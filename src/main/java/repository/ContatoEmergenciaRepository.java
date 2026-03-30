package repository;

import java.util.List;
import java.util.UUID;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import model.ContatoEmergencia;

@ApplicationScoped
public class ContatoEmergenciaRepository {

    @Inject
    EntityManager em;

    public List<ContatoEmergencia> listarPorPessoa(UUID pessoaId) {
        return em.createQuery(
                "FROM ContatoEmergencia c WHERE c.pessoa.id = :pessoaId ORDER BY c.nome", ContatoEmergencia.class)
                .setParameter("pessoaId", pessoaId)
                .getResultList();
    }

    public ContatoEmergencia buscarPorId(UUID id) {
        return em.find(ContatoEmergencia.class, id);
    }

    public void salvar(ContatoEmergencia contato) {
        em.persist(contato);
    }

    public void remover(ContatoEmergencia contato) {
        em.remove(em.contains(contato) ? contato : em.merge(contato));
    }
}