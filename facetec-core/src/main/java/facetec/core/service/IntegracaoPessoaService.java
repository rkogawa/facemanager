package facetec.core.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import facetec.core.dao.DeviceDAO;
import facetec.core.dao.IntegracaoPessoaDAO;
import facetec.core.dao.PessoaDAO;
import facetec.core.domain.Device;
import facetec.core.domain.IntegracaoPessoa;
import facetec.core.domain.Pessoa;
import facetec.core.domain.enumx.ClassificacaoDevice;
import facetec.core.domain.enumx.ModeloDevice;
import facetec.core.domain.enumx.StatusIntegracaoPessoa;
import facetec.core.security.domain.LocalidadeUsuario;
import facetec.core.security.service.SecurityService;
import facetec.core.service.integracao.DeviceKeyVO;
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

import java.util.ArrayList;
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
        LocalidadeUsuario localidade = securityService.getLocalidadeUsuario(usuario);
        List<IntegracaoPessoa> pessoasPendentes = dao.findPermanentesPendentes(localidade);
        pessoasPendentes.addAll(dao.findVisitantesPendentes(localidade));
        List<IntegracaoPessoaVO> integracoes = new ArrayList<>();

        Map<DeviceKeyVO, List<IntegracaoPessoaDeviceVO>> devicesPorChave = getBaseUrlDevices(usuario);
        for (IntegracaoPessoa pessoaPendente : pessoasPendentes) {

            devicesPorChave.keySet().forEach(deviceKey -> {
                IntegracaoPessoaVO integracaoVO = new IntegracaoPessoaVO();
                integracaoVO.setDevices(devicesPorChave.get(deviceKey));

                integracaoVO.setId(pessoaPendente.getId().toString());
                Pessoa pessoa = pessoaPendente.getPessoa();
                StatusIntegracaoPessoa statusEnviado;
                if (pessoaPendente.getStatus().isPendenteInclusao()) {
                    statusEnviado = createRequestInclusao(integracaoVO, pessoa, deviceKey);
                } else if (pessoaPendente.getStatus().isPendenteExclusao()) {
                    statusEnviado = createRequestExclusao(integracaoVO, pessoa, deviceKey);
                } else {
                    statusEnviado = createRequestAlteracao(integracaoVO, pessoa, deviceKey);
                }

                pessoaPendente.setStatus(statusEnviado);
                dao.save(pessoaPendente);
                integracoes.add(integracaoVO);
            });

        }

        return integracoes;
    }

    private Map<DeviceKeyVO, List<IntegracaoPessoaDeviceVO>> getBaseUrlDevices(String usuario) {
        List<Device> devices = deviceDAO.findBy(securityService.getLocalidadeUsuario(usuario));
        Map<DeviceKeyVO, List<IntegracaoPessoaDeviceVO>> devicesPorModelo = new HashMap<>();
        devices.forEach(d -> {
            DeviceKeyVO deviceKey = new DeviceKeyVO(d);
            devicesPorModelo.putIfAbsent(deviceKey, new ArrayList<>());
            IntegracaoPessoaDeviceVO vo = new IntegracaoPessoaDeviceVO();
            vo.setUrl(getStrategy(d.getModelo()).getBaseUrl(d));
            vo.setContentType(d.getModelo().getContentType());
            vo.setPassword(getStrategy(d.getModelo()).getPassword());
            devicesPorModelo.get(deviceKey).add(vo);
        });
        return devicesPorModelo;
    }

    private IntegracaoDeviceStrategy getStrategy(ModeloDevice modelo) {
        return integracaoPorModelo.get(modelo);
    }

    private StatusIntegracaoPessoa createRequestInclusao(IntegracaoPessoaVO integracaoVO, Pessoa pessoa, DeviceKeyVO device) {
        integracaoVO.getRequests().add(personCreate(pessoa, device));
        integracaoVO.getRequests().add(faceCreate(pessoa, device));
        if (pessoa.getDataHoraFim() != null) {
            integracaoVO.getRequests().add(permissionsCreate(pessoa, device));
        }
        return StatusIntegracaoPessoa.ENVIADO_INCLUSAO;
    }

    private StatusIntegracaoPessoa createRequestAlteracao(IntegracaoPessoaVO integracaoVO, Pessoa pessoa, DeviceKeyVO device) {
        integracaoVO.getRequests().add(personDelete(pessoa, device));
        integracaoVO.getRequests().add(personCreate(pessoa, device));
        integracaoVO.getRequests().add(faceCreate(pessoa, device));
        integracaoVO.getRequests().add(permissionsDelete(pessoa, device));
        if (ClassificacaoDevice.ENTRADA.equals(device.getClassificacao()) && pessoa.getDataHoraFim() != null) {
            integracaoVO.getRequests().add(permissionsCreate(pessoa, device));
        }
        return StatusIntegracaoPessoa.ENVIADO_ALTERACAO;
    }

    private StatusIntegracaoPessoa createRequestExclusao(IntegracaoPessoaVO integracaoVO, Pessoa pessoa, DeviceKeyVO device) {
        integracaoVO.getRequests().add(personDelete(pessoa, device));
        return StatusIntegracaoPessoa.ENVIADO_EXCLUSAO;
    }

    private IntegracaoPessoaRequestVO personCreate(Pessoa pessoa, DeviceKeyVO device) {
        IntegracaoPessoaRequestVO request = new IntegracaoPessoaRequestVO("person/create");

        PersonCreateVO paramPersonCreate = new PersonCreateVO(getPassword(device.getModelo()), new PersonVO(pessoa));
        createParamsJSON(request, paramPersonCreate);
        return request;
    }

    private String getPassword(ModeloDevice modelo) {
        return getStrategy(modelo).getPassword();
    }

    private IntegracaoPessoaRequestVO faceCreate(Pessoa pessoa, DeviceKeyVO device) {
        IntegracaoPessoaRequestVO request = new IntegracaoPessoaRequestVO("face/create");

        FaceCreateVO paramPersonCreate = new FaceCreateVO(getPassword(device.getModelo()), pessoa.getCpf(), pessoa.getFoto());
        createParamsJSON(request, paramPersonCreate);
        return request;
    }

    private IntegracaoPessoaRequestVO permissionsDelete(Pessoa pessoa, DeviceKeyVO device) {
        IntegracaoPessoaRequestVO request = new IntegracaoPessoaRequestVO("person/permissionsDelete");
        createParamsJSON(request, new PermissionDeleteVO(getPassword(device.getModelo()), pessoa.getCpf()));
        return request;
    }

    private IntegracaoPessoaRequestVO permissionsCreate(Pessoa pessoa, DeviceKeyVO device) {
        IntegracaoPessoaRequestVO request = new IntegracaoPessoaRequestVO("person/permissionsCreate");
        String time = integracaoPorModelo.get(device.getModelo()).getPermissionTime(pessoa);
        createParamsJSON(request, new PermissionCreateVO(getPassword(device.getModelo()), pessoa.getCpf(), time));
        return request;
    }

    private IntegracaoPessoaRequestVO personDelete(Pessoa pessoa, DeviceKeyVO device) {
        IntegracaoPessoaRequestVO request = new IntegracaoPessoaRequestVO("person/delete");

        PersonDeleteVO paramPersonDelete = new PersonDeleteVO(getPassword(device.getModelo()), pessoa.getCpf());
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
            status = statusAtual.isInclusao() ?
                    StatusIntegracaoPessoa.INCLUSAO_OK :
                    statusAtual.isAlteracao() ? StatusIntegracaoPessoa.ALTERACAO_OK : StatusIntegracaoPessoa.EXCLUSAO_OK;
        } else {
            status = statusAtual.isInclusao() ?
                    StatusIntegracaoPessoa.INCLUSAO_ERRO :
                    statusAtual.isAlteracao() ? StatusIntegracaoPessoa.ALTERACAO_ERRO : StatusIntegracaoPessoa.EXCLUSAO_ERRO;
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
