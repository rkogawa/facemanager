package facetec.core.dao;

import facetec.core.domain.Pessoa;
import facetec.core.domain.enumx.InformacaoAcessoPessoa;
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

    public Pessoa findByCpf(String cpf, LocalidadeUsuario localidade) {
        CriteriaBuilder criteriaBuilder = getSession().getCriteriaBuilder();
        CriteriaQuery<Pessoa> query = criteriaBuilder.createQuery(Pessoa.class);
        Root<Pessoa> root = query.from(Pessoa.class);
        query.select(root);
        query.where(criteriaBuilder.equal(root.get("cpf"), cpf), criteriaBuilder.equal(root.get("localidade"), localidade));
        return getSession().createQuery(query).getSingleResult();
    }

    public boolean existsBy(String cpf, LocalidadeUsuario localidade, Long id) {
        CriteriaBuilder criteriaBuilder = getSession().getCriteriaBuilder();
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        Root<Pessoa> root = query.from(Pessoa.class);
        query.select(criteriaBuilder.count(root));
        List<Predicate> whereConditions = new ArrayList();
        whereConditions.add(criteriaBuilder.equal(root.get("cpf"), cpf));
        whereConditions.add(criteriaBuilder.equal(root.get("localidade"), localidade));
        if (id != null) {
            whereConditions.add(criteriaBuilder.notEqual(root.get("id"), id));
        }
        query.where(whereConditions.toArray(new Predicate[whereConditions.size()]));
        return getSession().createQuery(query).getSingleResult() > 0L;
    }

    public void delete(Pessoa pessoa) {
        getSession().delete(pessoa);
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
