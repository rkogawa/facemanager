package facetec.core.controller;

import facetec.core.service.GrupoService;
import facetec.core.service.GrupoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by rkogawa on 17/05/19.
 */
@RestController
@RequestMapping("/grupo")
@Transactional
public class GrupoRestController {

    @Autowired
    private GrupoService service;

    @RequestMapping(method = RequestMethod.POST)
    public void create(@RequestBody GrupoVO grupo) {
        service.create(grupo);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public void update(@RequestBody GrupoVO grupo) {
        service.update(grupo);
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
