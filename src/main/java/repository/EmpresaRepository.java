package repository;

import java.util.List;
import java.util.UUID;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import model.Empresa;

@ApplicationScoped
public class EmpresaRepository {

    @Inject
    EntityManager em;

    public List<Empresa> listarAtivas() {
        return em.createQuery(
                "FROM Empresa e WHERE e.deletedAt IS NULL ORDER BY e.razaoSocial", Empresa.class)
                .getResultList();
    }

    public Empresa buscarPorId(UUID id) {
        return em.find(Empresa.class, id);
    }

    public void salvar(Empresa empresa) {
        em.persist(empresa);
    }
}