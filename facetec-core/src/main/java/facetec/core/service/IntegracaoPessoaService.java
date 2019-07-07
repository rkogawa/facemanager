package facetec.core.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import facetec.core.dao.DeviceDAO;
import facetec.core.dao.IntegracaoPessoaDAO;
import facetec.core.dao.PessoaDAO;
import facetec.core.domain.IntegracaoPessoa;
import facetec.core.domain.Pessoa;
import facetec.core.domain.enumx.ModeloDevice;
import facetec.core.domain.enumx.StatusIntegracaoPessoa;
import facetec.core.security.service.SecurityService;
import facetec.core.service.integracao.FaceCreateVO;
import facetec.core.service.integracao.IntegracaoDeviceStrategy;
import facetec.core.service.integracao.PermissionCreateVO;
import facetec.core.service.integracao.PermissionDeleteVO;
import facetec.core.service.integracao.PersonCreateVO;
import facetec.core.service.integracao.PersonDeleteVO;
import facetec.core.service.integracao.PersonVO;
import facetec.core.service.integracao.StatusIntegracaoPessoaVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by rkogawa on 21/05/19.
 */
@Service
public class IntegracaoPessoaService {

    @Autowired
    private IntegracaoPessoaDAO dao;

    @Autowired
    private DeviceDAO deviceDAO;

    @Autowired
    private PessoaDAO pessoaDAO;

    @Autowired
    private SecurityService securityService;

    public static final String PARAM_FIELD_PASSWORD = "<DEVICE_PASSWORD>";

    private Map<ModeloDevice, IntegracaoDeviceStrategy> integracaoPorModelo = new HashMap<>();

    @Autowired
    public void setIntegracaoPorModelo(List<IntegracaoDeviceStrategy> integracoes) {
        for (IntegracaoDeviceStrategy integracaoStrategy : integracoes) {
            integracaoPorModelo.put(integracaoStrategy.getModelo(), integracaoStrategy);
        }
    }

    public IntegracaoPessoa criarIntegracaoPessoa(Pessoa pessoa, StatusIntegracaoPessoa status) {
        IntegracaoPessoa integracaoPessoa = new IntegracaoPessoa();
        integracaoPessoa.setPessoa(pessoa);
        integracaoPessoa.setStatus(status);
        dao.save(integracaoPessoa);
        return integracaoPessoa;
    }

    public List<IntegracaoPessoaVO> findPendentes(String usuario) {
        List<IntegracaoPessoaVO> integracoes = dao.findPendentes(securityService.getLocalidadeUsuario(usuario)).stream().map(i -> {
            IntegracaoPessoaVO integracaoVO = new IntegracaoPessoaVO();
            integracaoVO.setId(i.getId().toString());
            Pessoa pessoa = i.getPessoa();
            StatusIntegracaoPessoa statusEnviado;
            if (i.getStatus().isPendenteInclusao()) {
                statusEnviado = createRequestInclusao(integracaoVO, pessoa);
            } else if (i.getStatus().isPendenteExclusao()) {
                statusEnviado = createRequestExclusao(integracaoVO, pessoa);
            } else {
                statusEnviado = createRequestAlteracao(integracaoVO, pessoa);
            }

            i.setStatus(statusEnviado);
            dao.save(i);
            return integracaoVO;
        }).collect(Collectors.toList());

        if (!integracoes.isEmpty()) {
            integracoes.forEach(i -> i.setDevices(getBaseUrlDevices(usuario)));
        }
        return integracoes;
    }

    private List<IntegracaoPessoaDeviceVO> getBaseUrlDevices(String usuario) {
        return deviceDAO.findBy(securityService.getLocalidadeUsuario(usuario)).stream().map(d -> {
            IntegracaoPessoaDeviceVO vo = new IntegracaoPessoaDeviceVO();
            vo.setUrl(getStrategy(d.getModelo()).getBaseUrl(d));
            vo.setContentType(d.getModelo().getContentType());
            vo.setPassword(getStrategy(d.getModelo()).getPassword());
            return vo;
        }).collect(Collectors.toList());
    }

    private IntegracaoDeviceStrategy getStrategy(ModeloDevice modelo) {
        return integracaoPorModelo.get(modelo);
    }

    private StatusIntegracaoPessoa createRequestInclusao(IntegracaoPessoaVO integracaoVO, Pessoa pessoa) {
        integracaoVO.getRequests().add(personCreate(pessoa));
        integracaoVO.getRequests().add(faceCreate(pessoa));
        if (pessoa.getDataHoraFim() != null) {
            integracaoVO.getRequests().add(permissionsCreate(pessoa));
        }
        return StatusIntegracaoPessoa.ENVIADO_INCLUSAO;
    }

    private StatusIntegracaoPessoa createRequestAlteracao(IntegracaoPessoaVO integracaoVO, Pessoa pessoa) {
        integracaoVO.getRequests().add(personCreate(pessoa));
        integracaoVO.getRequests().add(faceCreate(pessoa));
        integracaoVO.getRequests().add(permissionsDelete(pessoa));
        if (pessoa.getDataHoraFim() != null) {
            integracaoVO.getRequests().add(permissionsCreate(pessoa));
        }
        return StatusIntegracaoPessoa.ENVIADO_ALTERACAO;
    }

    private StatusIntegracaoPessoa createRequestExclusao(IntegracaoPessoaVO integracaoVO, Pessoa pessoa) {
        integracaoVO.getRequests().add(personDelete(pessoa));
        return StatusIntegracaoPessoa.ENVIADO_EXCLUSAO;
    }

    private IntegracaoPessoaRequestVO personCreate(Pessoa pessoa) {
        IntegracaoPessoaRequestVO request = new IntegracaoPessoaRequestVO("person/create");

        PersonCreateVO paramPersonCreate = new PersonCreateVO(new PersonVO(pessoa));
        createParamsJSON(request, paramPersonCreate);
        return request;
    }

    private IntegracaoPessoaRequestVO faceCreate(Pessoa pessoa) {
        IntegracaoPessoaRequestVO request = new IntegracaoPessoaRequestVO("face/create");

        FaceCreateVO paramPersonCreate = new FaceCreateVO(pessoa.getCpf(), pessoa.getFoto());
        createParamsJSON(request, paramPersonCreate);
        return request;
    }

    private IntegracaoPessoaRequestVO permissionsDelete(Pessoa pessoa) {
        IntegracaoPessoaRequestVO request = new IntegracaoPessoaRequestVO("person/permissionsDelete");
        createParamsJSON(request, new PermissionDeleteVO(pessoa.getCpf()));
        return request;
    }

    private IntegracaoPessoaRequestVO permissionsCreate(Pessoa pessoa) {
        IntegracaoPessoaRequestVO request = new IntegracaoPessoaRequestVO("person/permissionsCreate");
        createParamsJSON(request, new PermissionCreateVO(pessoa.getCpf(), Timestamp.valueOf(pessoa.getDataHoraFim()).getTime()));
        return request;
    }

    private IntegracaoPessoaRequestVO personDelete(Pessoa pessoa) {
        IntegracaoPessoaRequestVO request = new IntegracaoPessoaRequestVO("person/delete");

        PersonDeleteVO paramPersonDelete = new PersonDeleteVO(pessoa.getCpf());
        createParamsJSON(request, paramPersonDelete);
        return request;
    }

    private void createParamsJSON(IntegracaoPessoaRequestVO request, Object paramsVO) {
        try {
            request.setParamsJSON(new ObjectMapper().writeValueAsString(paramsVO));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateIntegracao(Long id, Boolean success, String log) {
        IntegracaoPessoa integracaoPessoa = dao.findById(id);
        StatusIntegracaoPessoa status;
        StatusIntegracaoPessoa statusAtual = integracaoPessoa.getStatus();
        if (success) {
            status = statusAtual.isEnviadoInclusao() ?
                    StatusIntegracaoPessoa.INCLUSAO_OK :
                    statusAtual.isEnviadoAlteracao() ? StatusIntegracaoPessoa.ALTERACAO_OK : StatusIntegracaoPessoa.EXCLUSAO_OK;
        } else {
            status = statusAtual.isEnviadoInclusao() ?
                    StatusIntegracaoPessoa.INCLUSAO_ERRO :
                    statusAtual.isEnviadoAlteracao() ? StatusIntegracaoPessoa.ALTERACAO_ERRO : StatusIntegracaoPessoa.EXCLUSAO_ERRO;
        }

        integracaoPessoa.setStatus(status);
        integracaoPessoa.setLogIntegracao(log);
        Pessoa pessoa = integracaoPessoa.getPessoa();
        if (status.isExclusao()) {
            integracaoPessoa.setPessoa(null);
        }
        dao.save(integracaoPessoa);

        if (status.isExclusao()) {
            dao.findBy(pessoa).forEach(i -> {
                i.setPessoa(null);
                dao.save(i);
            });
            pessoaDAO.delete(pessoa);
        }
    }

    public StatusIntegracaoPessoaVO getStatus(Long id) {
        return new StatusIntegracaoPessoaVO(dao.findById(id).getStatus().name());
    }

    public IntegracaoDevicesVO findDevices(String usuario) {
        IntegracaoDevicesVO vo = new IntegracaoDevicesVO();
        vo.setDevices(deviceDAO.findBy(securityService.getLocalidadeUsuario(usuario)).stream().map(d -> {
            IntegracaoDeviceVO device = new IntegracaoDeviceVO();
            device.setIp(d.getIp());
            device.setNome(d.getNome());
            device.setUrl(getStrategy(d.getModelo()).getBaseUrl(d));
            return device;
        }).collect(Collectors.toList()));

        vo.setRequestPath("getDeviceKey");
        return vo;
    }
}
