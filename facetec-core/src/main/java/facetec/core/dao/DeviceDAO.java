package facetec.core.dao;

import facetec.core.domain.Device;
import facetec.core.security.domain.LocalidadeUsuario;
import org.hibernate.Session;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Created by rkogawa on 09/05/19.
 */
@Component
@Transactional
public class DeviceDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public void save(Device device) {
        getSession().save(device);
    }

    private Session getSession() {
        return entityManager.unwrap(Session.class);
    }

    public List<Device> findBy(LocalidadeUsuario localidade) {
        CriteriaBuilder criteriaBuilder = getSession().getCriteriaBuilder();
        CriteriaQuery<Device> query = criteriaBuilder.createQuery(Device.class);
        Root<Device> root = query.from(Device.class);
        query.select(root);
        query.where(criteriaBuilder.equal(root.get("localidade"), localidade));
        query.orderBy(criteriaBuilder.asc(root.get("ip")));
        return getSession().createQuery(query).list();
    }

    public void delete(Device device) {
        getSession().delete(device);
    }
}
