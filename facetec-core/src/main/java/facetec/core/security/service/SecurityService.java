package facetec.core.security.service;

import facetec.core.security.dao.FaceTecUserDAO;
import facetec.core.security.domain.FaceTecUser;
import org.springframework.beans.factory.annotation.Autowired;
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

    public String getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null ? authentication.getName() : "Sistema";
    }

    public FaceTecUser getUser() {
        String currentUser = this.getCurrentUser();
        FaceTecUser user = userDAO.findBy(currentUser);
        if (user == null) {
            throw new RuntimeException("Não foi encontrado usuário " + currentUser);
        }
        return user;
    }
}
