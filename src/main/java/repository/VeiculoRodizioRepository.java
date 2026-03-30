package repository;

import java.util.List;
import java.util.UUID;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import model.VeiculoRodizio;

@ApplicationScoped
public class VeiculoRodizioRepository {

    @Inject
    EntityManager em;

    public List<VeiculoRodizio> listarAtivos() {
        return em.createQuery(
                "FROM VeiculoRodizio r WHERE r.status = TRUE ORDER BY r.finalPlaca", VeiculoRodizio.class)
                .getResultList();
    }

    public VeiculoRodizio buscarPorId(UUID id) {
        return em.find(VeiculoRodizio.class, id);
    }

    public void salvar(VeiculoRodizio rodizio) {
        em.persist(rodizio);
    }
}