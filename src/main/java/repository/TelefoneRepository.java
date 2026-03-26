package repository;

import java.util.List;
import java.util.UUID;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import model.Telefone;

@ApplicationScoped
public class TelefoneRepository {

    @Inject
    EntityManager em;

    public List<Telefone> listarPorPessoa(UUID pessoaId) {
        return em.createQuery(
                "FROM Telefone t WHERE t.pessoa.id = :pessoaId ORDER BY t.tipo", Telefone.class)
                .setParameter("pessoaId", pessoaId)
                .getResultList();
    }

    public Telefone buscarPorId(UUID id) {
        return em.find(Telefone.class, id);
    }

    public void salvar(Telefone telefone) {
        em.persist(telefone);
    }

    public void remover(Telefone telefone) {
        em.remove(em.contains(telefone) ? telefone : em.merge(telefone));
    }
}