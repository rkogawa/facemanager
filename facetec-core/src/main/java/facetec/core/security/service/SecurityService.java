package facetec.core.security.service;

import facetec.core.security.dao.FaceTecUserDAO;
import facetec.core.security.domain.FaceTecUser;
import facetec.core.security.domain.LocalidadeUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Created by rkogawa on 08/05/19.
 */
@Service
public class SecurityService {

    @Autowired
    private FaceTecUserDAO userDAO;

    @Value("${facetec.admin.password}")
    private String adminPassword;

    public String getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null ? authentication.getName() : "Sistema";
    }

    public LocalidadeUsuario getLocalidadeUsuario() {
        return getLocalidadeUsuario(getCurrentUser());
    }

    public LocalidadeUsuario getLocalidadeUsuario(String usuario) {
        FaceTecUser user = userDAO.findBy(usuario);
        if (user == null) {
            throw new RuntimeException("Não foi encontrado usuário " + usuario);
        }
        return user.getLocalidade();
    }

    public boolean checkAdminPassword(String pass) {
        return adminPassword.equals(pass);
    }
}
