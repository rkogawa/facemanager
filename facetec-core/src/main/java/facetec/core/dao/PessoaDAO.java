package facetec.core.dao;

import facetec.core.domain.Pessoa;
import org.hibernate.Session;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * Created by rkogawa on 08/05/19.
 */
@Component
@Transactional
public class PessoaDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public void save(Pessoa pessoa) {
        getSession().save(pessoa);
    }

    private Session getSession() {
        return entityManager.unwrap(Session.class);
    }

    public Pessoa findByCpf(String cpf) {
        CriteriaBuilder criteriaBuilder = getSession().getCriteriaBuilder();
        CriteriaQuery<Pessoa> query = criteriaBuilder.createQuery(Pessoa.class);
        Root<Pessoa> root = query.from(Pessoa.class);
        query.select(root);
        query.where(criteriaBuilder.equal(root.get("cpf"), cpf));
        return getSession().createQuery(query).getSingleResult();
    }

    public void delete(Long id) {
        getSession().delete(entityManager.getReference(Pessoa.class, id));
    }
}
