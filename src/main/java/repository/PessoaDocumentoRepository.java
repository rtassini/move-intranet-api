package repository;

import java.util.UUID;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import model.PessoaDocumento;

@ApplicationScoped
public class PessoaDocumentoRepository {

    @Inject
    EntityManager em;

    public PessoaDocumento buscarPorPessoa(UUID pessoaId) {
        return em.createQuery(
                "FROM PessoaDocumento pd WHERE pd.pessoa.id = :pessoaId", PessoaDocumento.class)
                .setParameter("pessoaId", pessoaId)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    public void salvar(PessoaDocumento doc) {
        em.persist(doc);
    }
}