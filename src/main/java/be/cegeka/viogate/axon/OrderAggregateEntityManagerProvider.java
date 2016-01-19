package be.cegeka.viogate.axon;

import org.axonframework.common.jpa.EntityManagerProvider;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
public class OrderAggregateEntityManagerProvider implements EntityManagerProvider {

    private EntityManager entityManager;

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    @PersistenceContext(unitName = "eventStore")
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

}
