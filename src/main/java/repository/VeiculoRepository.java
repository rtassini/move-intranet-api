package repository;

import java.util.List;
import java.util.UUID;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import model.Veiculo;

@ApplicationScoped
public class VeiculoRepository {

    @Inject
    EntityManager em;

    public List<Veiculo> listarAtivos() {
        return em.createQuery(
                "FROM Veiculo v JOIN FETCH v.empresa WHERE v.deletedAt IS NULL ORDER BY v.placa", Veiculo.class)
                .getResultList();
    }

    public List<Veiculo> listarDisponiveis() {
        return em.createQuery(
                "FROM Veiculo v JOIN FETCH v.empresa " +
                "WHERE v.deletedAt IS NULL AND v.situacao = 'DISPONIVEL' ORDER BY v.placa", Veiculo.class)
                .getResultList();
    }

    public Veiculo buscarPorId(UUID id) {
        return em.find(Veiculo.class, id);
    }

    public void salvar(Veiculo veiculo) {
        em.persist(veiculo);
    }
}