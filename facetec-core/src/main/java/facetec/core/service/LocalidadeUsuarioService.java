package facetec.core.service;

import facetec.core.dao.LocalidadeUsuarioDAO;
import facetec.core.security.domain.LocalidadeUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by rkogawa on 01/07/19.
 */
@Service
public class LocalidadeUsuarioService {

    @Autowired
    private LocalidadeUsuarioDAO dao;

    public void create(LocalidadeUsuarioVO vo) {
        LocalidadeUsuario localidade = new LocalidadeUsuario();
        saveLocalidade(vo, localidade);
    }

    private void saveLocalidade(LocalidadeUsuarioVO vo, LocalidadeUsuario localidade) {
        localidade.setNome(vo.getNome());
        if (dao.existsBy(localidade.getNome(), localidade.getId())) {
            throw new RuntimeException("Já existe localidade cadastrado com nome " + localidade.getNome());
        }
        dao.save(localidade);
    }

    public void update(LocalidadeUsuarioVO vo) {
        LocalidadeUsuario localidade = dao.findBy(vo.getNomeOriginal());
        saveLocalidade(vo, localidade);
    }

    public void delete(String nome) {
        dao.delete(dao.findBy(nome));
    }

    public List<String> list() {
        return dao.findAll().stream().map(g -> g.getNome()).collect(Collectors.toList());
    }
}
