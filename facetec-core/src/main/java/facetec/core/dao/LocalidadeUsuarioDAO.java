package facetec.core.dao;

import facetec.core.security.domain.LocalidadeUsuario;
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
 * Created by rkogawa on 01/07/19.
 */
@Component
@Transactional
public class LocalidadeUsuarioDAO {

    @PersistenceContext
    private EntityManager entityManager;

    private Session getSession() {
        return entityManager.unwrap(Session.class);
    }

    public boolean existsBy(String nome, Long id) {
        CriteriaBuilder criteriaBuilder = getSession().getCriteriaBuilder();
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        Root<LocalidadeUsuario> root = query.from(LocalidadeUsuario.class);
        query.select(criteriaBuilder.count(root));
        List<Predicate> whereConditions = new ArrayList();
        whereConditions.add(criteriaBuilder.equal(root.get("nome"), nome));
        if (id != null) {
            whereConditions.add(criteriaBuilder.notEqual(root.get("id"), id));
        }
        query.where(whereConditions.toArray(new Predicate[whereConditions.size()]));
        return getSession().createQuery(query).getSingleResult() > 0L;
    }

    public void save(LocalidadeUsuario localidade) {
        getSession().save(localidade);
    }

    public LocalidadeUsuario findBy(String nome) {
        CriteriaBuilder criteriaBuilder = getSession().getCriteriaBuilder();
        CriteriaQuery<LocalidadeUsuario> query = criteriaBuilder.createQuery(LocalidadeUsuario.class);
        Root<LocalidadeUsuario> root = query.from(LocalidadeUsuario.class);
        query.select(root);
        query.where(criteriaBuilder.equal(root.get("nome"), nome));
        return getSession().createQuery(query).getSingleResult();
    }

    public void delete(LocalidadeUsuario localidade) {
        getSession().delete(localidade);
    }

    public List<LocalidadeUsuario> findAll() {
        CriteriaBuilder criteriaBuilder = getSession().getCriteriaBuilder();
        CriteriaQuery<LocalidadeUsuario> query = criteriaBuilder.createQuery(LocalidadeUsuario.class);
        Root<LocalidadeUsuario> root = query.from(LocalidadeUsuario.class);
        query.select(root);
        query.orderBy(criteriaBuilder.asc(root.get("nome")));
        return getSession().createQuery(query).list();
    }
}
