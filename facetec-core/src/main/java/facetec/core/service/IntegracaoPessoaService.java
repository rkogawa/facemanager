package facetec.core.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import facetec.core.dao.DeviceDAO;
import facetec.core.dao.IntegracaoPessoaDAO;
import facetec.core.dao.PessoaDAO;
import facetec.core.domain.Device;
import facetec.core.domain.IntegracaoPessoa;
import facetec.core.domain.Pessoa;
import facetec.core.domain.enumx.StatusIntegracaoPessoa;
import facetec.core.security.service.SecurityService;
import facetec.core.service.integracao.FaceCreateVO;
import facetec.core.service.integracao.PermissionCreateVO;
import facetec.core.service.integracao.PermissionDeleteVO;
import facetec.core.service.integracao.PersonCreateVO;
import facetec.core.service.integracao.PersonDeleteVO;
import facetec.core.service.integracao.PersonVO;
import facetec.core.service.integracao.StatusIntegracaoPessoaVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
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

    private static final String DEVICE_PASSWORD = "12345";

    @Value("${facetec.client.deviceBaseUrl:http://%s:8088/}")
    private String deviceBaseUrl;

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
            List<String> devicesUrl = getBaseUrlDevices(usuario);
            integracoes.forEach(i -> i.setDevices(devicesUrl));
        }
        return integracoes;
    }

    private List<String> getBaseUrlDevices(String usuario) {
        return deviceDAO.findBy(securityService.getLocalidadeUsuario(usuario)).stream().map(d -> getUrl(d)).collect(Collectors.toList());
    }

    private String getUrl(Device d) {
        return String.format(deviceBaseUrl, d.getIp());
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

        PersonCreateVO paramPersonCreate = new PersonCreateVO(DEVICE_PASSWORD, new PersonVO(pessoa));
        createParamsJSON(request, paramPersonCreate);
        return request;
    }

    private IntegracaoPessoaRequestVO faceCreate(Pessoa pessoa) {
        IntegracaoPessoaRequestVO request = new IntegracaoPessoaRequestVO("face/create");

        FaceCreateVO paramPersonCreate = new FaceCreateVO(DEVICE_PASSWORD, pessoa.getCpf(), pessoa.getFoto());
        createParamsJSON(request, paramPersonCreate);
        return request;
    }

    private IntegracaoPessoaRequestVO permissionsDelete(Pessoa pessoa) {
        IntegracaoPessoaRequestVO request = new IntegracaoPessoaRequestVO("person/permissionsDelete");
        createParamsJSON(request, new PermissionDeleteVO(DEVICE_PASSWORD, pessoa.getCpf()));
        return request;
    }

    private IntegracaoPessoaRequestVO permissionsCreate(Pessoa pessoa) {
        IntegracaoPessoaRequestVO request = new IntegracaoPessoaRequestVO("person/permissionsCreate");
        createParamsJSON(request, new PermissionCreateVO(DEVICE_PASSWORD, pessoa.getCpf(), Timestamp.valueOf(pessoa.getDataHoraFim()).getTime()));
        return request;
    }

    private IntegracaoPessoaRequestVO personDelete(Pessoa pessoa) {
        IntegracaoPessoaRequestVO request = new IntegracaoPessoaRequestVO("person/delete");

        PersonDeleteVO paramPersonDelete = new PersonDeleteVO(DEVICE_PASSWORD, pessoa.getCpf());
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
            device.setUrl(getUrl(d));
            return device;
        }).collect(Collectors.toList()));

        vo.setRequestPath("getDeviceKey");
        return vo;
    }
}
