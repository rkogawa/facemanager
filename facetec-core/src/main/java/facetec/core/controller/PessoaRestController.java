package facetec.core.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import facetec.core.service.PessoaResponseVO;
import facetec.core.service.PessoaService;
import facetec.core.service.PessoaVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * Created by rkogawa on 08/05/19.
 */
@RestController
@RequestMapping("/pessoa")
@Transactional
public class PessoaRestController {

    @Autowired
    private PessoaService service;

    @RequestMapping(method = RequestMethod.POST)
    public PessoaResponseVO create(@RequestParam Map<String, Object> params, @RequestParam(required = false) MultipartFile fileFoto) {
        params.remove("fileFoto");
        PessoaVO pessoa = new ObjectMapper().convertValue(params, PessoaVO.class);
        return service.create(pessoa, fileFoto);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public PessoaResponseVO update(@RequestParam Map<String, Object> params, @RequestParam(required = false) MultipartFile fileFoto) {
        params.remove("fileFoto");
        PessoaVO pessoa = new ObjectMapper().convertValue(params, PessoaVO.class);
        return service.update(pessoa, fileFoto);
    }

    @RequestMapping(value = "/{cpf}", method = RequestMethod.GET)
    public PessoaVO findByCpf(@PathVariable String cpf) {
        return service.findByCpf(cpf);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

}
