package facetec.core.controller;

import facetec.core.service.LocalidadeUsuarioService;
import facetec.core.service.LocalidadeUsuarioVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by rkogawa on 01/07/19.
 */
@RestController
@RequestMapping("/localidade")
@Transactional
public class LocalidadeUsuarioRestController {

    @Autowired
    private LocalidadeUsuarioService service;

    @RequestMapping(method = RequestMethod.POST)
    public void create(@RequestBody LocalidadeUsuarioVO localidadeVO) {
        service.create(localidadeVO);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public void update(@RequestBody LocalidadeUsuarioVO localidadeVO) {
        service.update(localidadeVO);
    }

    @RequestMapping(value = "/{nome}", method = RequestMethod.DELETE)
    public void delete(@PathVariable String nome) {
        service.delete(nome);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/list")
    public List<String> list() {
        return service.list();
    }

}