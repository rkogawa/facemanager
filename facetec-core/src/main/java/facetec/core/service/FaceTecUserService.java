package facetec.core.service;

import facetec.core.security.dao.FaceTecUserDAO;
import facetec.core.security.domain.FaceTecUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by rkogawa on 17/05/19.
 */
@Service
public class FaceTecUserService {

    @Autowired
    private FaceTecUserDAO dao;

    public void create(FaceTecUserVO vo) {
        FaceTecUser usuario = new FaceTecUser();
        usuario.setPassword(encodePassword(vo));
        saveUsuario(vo, usuario);
    }

    private String encodePassword(FaceTecUserVO vo) {
        return new BCryptPasswordEncoder(11).encode(vo.getPassword());
    }

    private void saveUsuario(FaceTecUserVO vo, FaceTecUser usuario) {
        usuario.setUsername(vo.getUsername());
        usuario.setAdmin(vo.isAdmin());
        if (dao.existsBy(vo.getUsername(), vo.getId())) {
            throw new RuntimeException("Já existe usuário " + vo.getUsername() + " cadastrado.");
        }
        dao.save(usuario);
    }

    public void update(FaceTecUserVO vo) {
        FaceTecUser usuario = dao.findById(vo.getId());
        if (vo.isChangePassword()) {
            usuario.setPassword(encodePassword(vo));
        }
        saveUsuario(vo, usuario);
    }

    public void delete(Long id) {
        dao.delete(id);
    }

    public List<String> list() {
        return dao.findAll().stream().map(FaceTecUser::getUsername).collect(Collectors.toList());
    }

    public FaceTecUserVO findBy(String username) {
        FaceTecUser usuario = dao.findBy(username);
        FaceTecUserVO vo = new FaceTecUserVO();
        vo.setId(usuario.getId());
        vo.setUsername(usuario.getUsername());
        vo.setAdmin(usuario.isAdmin());
        return vo;
    }
}
