package facetec.core.security.dao;

import facetec.core.security.domain.FaceTecUser;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by rkogawa on 06/05/19.
 */
@Component
@Transactional
public class FaceTecUserDAO {
    @PersistenceContext
    private EntityManager entityManager;

    public FaceTecUser findBy(String username) {
        Criteria criteria = getSession().createCriteria(FaceTecUser.class);
        criteria.add(Restrictions.eq("username", username));
        return (FaceTecUser) criteria.uniqueResult();
    }

    private Session getSession() {
        return entityManager.unwrap(Session.class);
    }
}
