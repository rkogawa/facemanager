package facetec.core.dao;

import facetec.core.domain.Pessoa;
import facetec.core.domain.enumx.InformacaoAcessoPessoa;
import facetec.core.security.domain.FaceTecUser;
import org.hibernate.Session;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    public Pessoa findByCpf(String cpf, FaceTecUser user) {
        CriteriaBuilder criteriaBuilder = getSession().getCriteriaBuilder();
        CriteriaQuery<Pessoa> query = criteriaBuilder.createQuery(Pessoa.class);
        Root<Pessoa> root = query.from(Pessoa.class);
        query.select(root);
        query.where(criteriaBuilder.equal(root.get("cpf"), cpf), criteriaBuilder.equal(root.get("predio"), user));
        return getSession().createQuery(query).getSingleResult();
    }

    public List<Pessoa> findByUser(String user) {
        CriteriaBuilder criteriaBuilder = getSession().getCriteriaBuilder();
        CriteriaQuery<Pessoa> query = criteriaBuilder.createQuery(Pessoa.class);
        Root<Pessoa> root = query.from(Pessoa.class);
        Join<Pessoa, FaceTecUser> join = root.join("predio");
        query.select(root);
        query.where(criteriaBuilder.equal(join.get("username"), user));
        return getSession().createQuery(query).list();
    }

    public boolean existsBy(String cpf, FaceTecUser user, Long id) {
        CriteriaBuilder criteriaBuilder = getSession().getCriteriaBuilder();
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        Root<Pessoa> root = query.from(Pessoa.class);
        query.select(criteriaBuilder.count(root));
        List<Predicate> whereConditions = new ArrayList();
        whereConditions.add(criteriaBuilder.equal(root.get("cpf"), cpf));
        whereConditions.add(criteriaBuilder.equal(root.get("predio"), user));
        if (id != null) {
            whereConditions.add(criteriaBuilder.notEqual(root.get("id"), id));
        }
        query.where(whereConditions.toArray(new Predicate[whereConditions.size()]));
        return getSession().createQuery(query).getSingleResult() > 0L;
    }

    public void delete(Long id) {
        getSession().delete(entityManager.getReference(Pessoa.class, id));
    }

    public Pessoa findById(Long id) {
        CriteriaBuilder criteriaBuilder = getSession().getCriteriaBuilder();
        CriteriaQuery<Pessoa> query = criteriaBuilder.createQuery(Pessoa.class);
        Root<Pessoa> root = query.from(Pessoa.class);
        query.select(root);
        query.where(criteriaBuilder.equal(root.get("id"), id));
        return getSession().createQuery(query).getSingleResult();
    }

    public List<Pessoa> findBVisitantesBefore(LocalDateTime dataHoraExclusao) {
        CriteriaBuilder criteriaBuilder = getSession().getCriteriaBuilder();
        CriteriaQuery<Pessoa> query = criteriaBuilder.createQuery(Pessoa.class);
        Root<Pessoa> root = query.from(Pessoa.class);
        query.select(root);
        query.where(criteriaBuilder.lessThan(root.get("dataHoraRegistro"), dataHoraExclusao), criteriaBuilder.equal(root.get("informacaoAcesso"),
                InformacaoAcessoPessoa.VISITANTE));
        return getSession().createQuery(query).list();
    }
}
