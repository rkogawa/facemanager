package facetec.core.security.service;

import facetec.core.security.dao.FaceTecUserDAO;
import facetec.core.security.domain.FaceTecUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by rkogawa on 06/05/19.
 */
@Service
public class FaceTecUserDetailsService implements UserDetailsService {

    @Autowired
    private FaceTecUserDAO dao;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        FaceTecUser user = dao.findBy(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new FaceTecUserPrincipal(user);
    }
}
