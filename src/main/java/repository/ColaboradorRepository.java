package repository;

import java.util.List;
import java.util.UUID;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import model.Colaborador;

@ApplicationScoped
public class ColaboradorRepository {

    @Inject
    EntityManager em;

    public List<Colaborador> listarAtivos() {
        return em.createQuery(
                "FROM Colaborador c JOIN FETCH c.pessoa JOIN FETCH c.empresa " +
                "WHERE c.deletedAt IS NULL ORDER BY c.pessoa.nomeCompleto", Colaborador.class)
                .getResultList();
    }

    public Colaborador buscarPorId(UUID id) {
        return em.find(Colaborador.class, id);
    }

    public void salvar(Colaborador colaborador) {
        em.persist(colaborador);
    }
}