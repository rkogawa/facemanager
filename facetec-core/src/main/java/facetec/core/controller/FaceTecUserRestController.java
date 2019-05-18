package facetec.core.controller;

import facetec.core.service.FaceTecUserService;
import facetec.core.service.FaceTecUserVO;
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
@RequestMapping("/usuario")
@Transactional
public class FaceTecUserRestController {

    @Autowired
    private FaceTecUserService service;

    @RequestMapping(method = RequestMethod.POST)
    public void create(@RequestBody FaceTecUserVO usuario) {
        service.create(usuario);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public void update(@RequestBody FaceTecUserVO usuario) {
        service.update(usuario);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/list")
    public List<String> list() {
        return service.list();
    }

    @RequestMapping(value = "/find/{username}", method = RequestMethod.GET)
    public FaceTecUserVO findByUsername(@PathVariable String username) {
        return service.findBy(username);
    }
}
