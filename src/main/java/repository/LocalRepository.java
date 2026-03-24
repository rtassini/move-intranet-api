package repository;

import java.util.List;
import java.util.UUID;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import model.Local;

@ApplicationScoped
public class LocalRepository {

    @Inject
    EntityManager em;

    public List<Local> listarAtivos() {
        return em.createQuery(
                "FROM Local l WHERE l.status = TRUE ORDER BY l.descricao", Local.class)
                .getResultList();
    }

    public Local buscarPorId(UUID id) {
        return em.find(Local.class, id);
    }

    public void salvar(Local local) {
        em.persist(local);
    }
}