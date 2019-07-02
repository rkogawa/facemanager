package facetec.core.dao;

import facetec.core.domain.Grupo;
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
 * Created by rkogawa on 17/05/19.
 */
@Component
@Transactional
public class GrupoDAO {

    @PersistenceContext
    private EntityManager entityManager;

    private Session getSession() {
        return entityManager.unwrap(Session.class);
    }

    public void save(Grupo grupo) {
        getSession().save(grupo);
    }

    public Grupo findBy(String nome, LocalidadeUsuario localidade) {
        CriteriaBuilder criteriaBuilder = getSession().getCriteriaBuilder();
        CriteriaQuery<Grupo> query = criteriaBuilder.createQuery(Grupo.class);
        Root<Grupo> root = query.from(Grupo.class);
        query.select(root);
        query.where(criteriaBuilder.equal(root.get("nome"), nome), criteriaBuilder.equal(root.get("localidade"), localidade));
        return getSession().createQuery(query).getSingleResult();
    }

    public void delete(Grupo grupo) {
        getSession().delete(grupo);
    }

    public List<Grupo> findAll(LocalidadeUsuario localidade) {
        CriteriaBuilder criteriaBuilder = getSession().getCriteriaBuilder();
        CriteriaQuery<Grupo> query = criteriaBuilder.createQuery(Grupo.class);
        Root<Grupo> root = query.from(Grupo.class);
        query.select(root);
        query.where(criteriaBuilder.equal(root.get("localidade"), localidade));
        query.orderBy(criteriaBuilder.asc(root.get("nome")));
        return getSession().createQuery(query).list();
    }

    public boolean existsBy(String nome, LocalidadeUsuario localidade, Long id) {
        CriteriaBuilder criteriaBuilder = getSession().getCriteriaBuilder();
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        Root<Grupo> root = query.from(Grupo.class);
        query.select(criteriaBuilder.count(root));
        List<Predicate> whereConditions = new ArrayList();
        whereConditions.add(criteriaBuilder.equal(root.get("nome"), nome));
        whereConditions.add(criteriaBuilder.equal(root.get("localidade"), localidade));
        if (id != null) {
            whereConditions.add(criteriaBuilder.notEqual(root.get("id"), id));
        }
        query.where(whereConditions.toArray(new Predicate[whereConditions.size()]));
        return getSession().createQuery(query).getSingleResult() > 0L;
    }
}
