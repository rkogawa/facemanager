package facetec.core.service;

import facetec.core.dao.GrupoDAO;
import facetec.core.domain.Grupo;
import facetec.core.security.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by rkogawa on 17/05/19.
 */
@Service
public class GrupoService {

    @Autowired
    private GrupoDAO dao;

    @Autowired
    private SecurityService securityService;

    public void create(GrupoVO vo) {
        Grupo grupo = new Grupo();
        grupo.setLocalidade(securityService.getLocalidadeUsuario());
        saveGrupo(vo, grupo);
    }

    private void saveGrupo(GrupoVO vo, Grupo grupo) {
        grupo.setNome(vo.getNome());
        if (dao.existsBy(grupo.getNome(), grupo.getLocalidade(), grupo.getId())) {
            throw new RuntimeException("Já existe grupo cadastrado com nome " + grupo.getNome());
        }
        dao.save(grupo);
    }

    public void update(GrupoVO vo) {
        Grupo grupo = dao.findBy(vo.getNomeOriginal(), securityService.getLocalidadeUsuario());
        saveGrupo(vo, grupo);
    }

    public void delete(String nome) {
        dao.delete(dao.findBy(nome, securityService.getLocalidadeUsuario()));
    }

    public List<String> list() {
        return dao.findAll(securityService.getLocalidadeUsuario()).stream().map(g -> g.getNome()).collect(Collectors.toList());
    }
}
