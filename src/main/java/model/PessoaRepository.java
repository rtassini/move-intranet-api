package model;

import java.util.List;
import java.util.UUID;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

@ApplicationScoped
public class PessoaRepository {

    @Inject
    EntityManager em;

    public List<Pessoa> listarAtivas() {
        return em.createQuery(
                "FROM Pessoa p WHERE p.deletedAt IS NULL ORDER BY p.nomeCompleto", Pessoa.class)
                .getResultList();
    }

    public Pessoa buscarPorId(UUID id) {
        return em.find(Pessoa.class, id);
    }

    public void salvar(Pessoa pessoa) {
        em.persist(pessoa);
    }
}