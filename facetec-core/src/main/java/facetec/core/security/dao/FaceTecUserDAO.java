package facetec.core.security.dao;

import facetec.core.security.domain.FaceTecUser;
import org.hibernate.Session;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rkogawa on 06/05/19.
 */
@Component
@Transactional
public class FaceTecUserDAO {
    @PersistenceContext
    private EntityManager entityManager;

    public FaceTecUser findBy(String username) {
        CriteriaBuilder criteriaBuilder = getSession().getCriteriaBuilder();
        CriteriaQuery<FaceTecUser> query = criteriaBuilder.createQuery(FaceTecUser.class);
        Root<FaceTecUser> root = query.from(FaceTecUser.class);
        query.select(root);
        query.where(criteriaBuilder.equal(root.get("username"), username));
        return getSession().createQuery(query).getSingleResult();
    }

    private Session getSession() {
        return entityManager.unwrap(Session.class);
    }

    public List<FaceTecUser> findAll() {
        CriteriaBuilder criteriaBuilder = getSession().getCriteriaBuilder();
        CriteriaQuery<FaceTecUser> query = criteriaBuilder.createQuery(FaceTecUser.class);
        Root<FaceTecUser> root = query.from(FaceTecUser.class);
        query.select(root);
        query.orderBy(criteriaBuilder.asc(root.get("username")));
        return getSession().createQuery(query).list();
    }

    public void save(FaceTecUser usuario) {
        getSession().save(usuario);
    }

    public FaceTecUser findById(Long id) {
        CriteriaBuilder criteriaBuilder = getSession().getCriteriaBuilder();
        CriteriaQuery<FaceTecUser> query = criteriaBuilder.createQuery(FaceTecUser.class);
        Root<FaceTecUser> root = query.from(FaceTecUser.class);
        query.select(root);
        query.where(criteriaBuilder.equal(root.get("id"), id));
        return getSession().createQuery(query).getSingleResult();
    }

    public boolean existsBy(String username, Long id) {
        CriteriaBuilder criteriaBuilder = getSession().getCriteriaBuilder();
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        Root<FaceTecUser> root = query.from(FaceTecUser.class);
        query.select(criteriaBuilder.count(root));
        List<Predicate> whereConditions = new ArrayList();
        whereConditions.add(criteriaBuilder.equal(root.get("username"), username));
        if (id != null) {
            whereConditions.add(criteriaBuilder.notEqual(root.get("id"), id));
        }
        query.where(whereConditions.toArray(new Predicate[whereConditions.size()]));
        return getSession().createQuery(query).getSingleResult() > 0L;
    }

    public void delete(Long id) {
        getSession().delete(entityManager.getReference(FaceTecUser.class, id));
    }
}
