package facetec.core.dao;

import facetec.core.domain.IntegracaoPessoa;
import facetec.core.domain.Pessoa;
import facetec.core.domain.enumx.InformacaoAcessoPessoa;
import facetec.core.domain.enumx.StatusIntegracaoPessoa;
import facetec.core.security.domain.LocalidadeUsuario;
import org.hibernate.Session;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by rkogawa on 21/05/19.
 */
@Component
@Transactional
public class IntegracaoPessoaDAO {

    @PersistenceContext
    private EntityManager entityManager;

    private Session getSession() {
        return entityManager.unwrap(Session.class);
    }

    public void save(IntegracaoPessoa integracaoPessoa) {
        getSession().save(integracaoPessoa);
    }

    public List<IntegracaoPessoa> findPermanentesPendentes(LocalidadeUsuario localidade) {
        CriteriaBuilder criteriaBuilder = getSession().getCriteriaBuilder();
        CriteriaQuery<IntegracaoPessoa> query = criteriaBuilder.createQuery(IntegracaoPessoa.class);
        Root<IntegracaoPessoa> root = query.from(IntegracaoPessoa.class);
        Join<IntegracaoPessoa, Pessoa> joinPessoa = root.join("pessoa");
        query.select(root);

        Expression<StatusIntegracaoPessoa> exp = root.get("status");
        Predicate pendentesCondition = exp.in(StatusIntegracaoPessoa.PENDENTE_INCLUSAO, StatusIntegracaoPessoa.PENDENTE_ALTERACAO, StatusIntegracaoPessoa.PENDENTE_EXCLUSAO);
        query.where(criteriaBuilder.equal(joinPessoa.get("localidade"), localidade), pendentesCondition, criteriaBuilder.equal(joinPessoa.get("informacaoAcesso"),
                InformacaoAcessoPessoa.PERMANENTE));
        return getSession().createQuery(query).list();
    }

    public List<IntegracaoPessoa> findVisitantesPendentes(LocalidadeUsuario localidade) {
        CriteriaBuilder criteriaBuilder = getSession().getCriteriaBuilder();
        CriteriaQuery<IntegracaoPessoa> query = criteriaBuilder.createQuery(IntegracaoPessoa.class);
        Root<IntegracaoPessoa> root = query.from(IntegracaoPessoa.class);
        Join<IntegracaoPessoa, Pessoa> joinPessoa = root.join("pessoa");
        query.select(root);

        Expression<StatusIntegracaoPessoa> exp = root.get("status");
        Predicate pendentesCondition = exp.in(StatusIntegracaoPessoa.PENDENTE_INCLUSAO, StatusIntegracaoPessoa.PENDENTE_ALTERACAO, StatusIntegracaoPessoa.PENDENTE_EXCLUSAO);
        query.where(criteriaBuilder.equal(joinPessoa.get("localidade"), localidade), pendentesCondition, criteriaBuilder.equal(joinPessoa.get("informacaoAcesso"),
                InformacaoAcessoPessoa.VISITANTE), criteriaBuilder.lessThanOrEqualTo(joinPessoa.get("dataHoraInicio"), LocalDateTime.now()));
        return getSession().createQuery(query).list();
    }

    public IntegracaoPessoa findById(Long id) {
        CriteriaBuilder criteriaBuilder = getSession().getCriteriaBuilder();
        CriteriaQuery<IntegracaoPessoa> query = criteriaBuilder.createQuery(IntegracaoPessoa.class);
        Root<IntegracaoPessoa> root = query.from(IntegracaoPessoa.class);
        query.select(root);
        query.where(criteriaBuilder.equal(root.get("id"), id));
        return getSession().createQuery(query).getSingleResult();
    }

    public List<IntegracaoPessoa> findBy(Pessoa pessoa) {
        CriteriaBuilder criteriaBuilder = getSession().getCriteriaBuilder();
        CriteriaQuery<IntegracaoPessoa> query = criteriaBuilder.createQuery(IntegracaoPessoa.class);
        Root<IntegracaoPessoa> root = query.from(IntegracaoPessoa.class);
        query.select(root);
        query.where(criteriaBuilder.equal(root.get("pessoa"), pessoa));
        return getSession().createQuery(query).list();
    }
}
