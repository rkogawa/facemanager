package facetec.core.controller;

import facetec.core.service.IntegracaoDevicesVO;
import facetec.core.service.IntegracaoPessoaService;
import facetec.core.service.IntegracaoPessoaVO;
import facetec.core.service.integracao.StatusIntegracaoPessoaVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Created by rkogawa on 20/05/19.
 */
@RestController
@RequestMapping("/integracaoPessoa")
@Transactional
public class IntegracaoPessoaRestController {

    @Autowired
    private IntegracaoPessoaService service;

    @RequestMapping(value = "/pendentes/{usuario}", method = RequestMethod.GET)
    public List<IntegracaoPessoaVO> findPendentes(@PathVariable String usuario) {
        return service.findPendentes(usuario);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public void updateIntegracao(@PathVariable Long id, @RequestBody Map<String, Object> params) {
        service.updateIntegracao(id, (Boolean) params.get("success"), null);
    }

    @RequestMapping(value = "/status/{id}", method = RequestMethod.GET)
    public StatusIntegracaoPessoaVO getStatus(@PathVariable Long id) {
        return service.getStatus(id);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/devices/{usuario}")
    public IntegracaoDevicesVO findDevices(@PathVariable String usuario) {
        return service.findDevices(usuario);
    }
}
