package repository;

import java.util.List;
import java.util.UUID;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import model.VeiculoUso;

@ApplicationScoped
public class VeiculoUsoRepository {

    @Inject
    EntityManager em;

    public List<VeiculoUso> listarTodos() {
        return em.createQuery(
                "SELECT u FROM VeiculoUso u " +
                "JOIN FETCH u.veiculo v JOIN FETCH v.empresa " +
                "JOIN FETCH u.colaborador c JOIN FETCH c.pessoa JOIN FETCH c.empresa " +
                "JOIN FETCH u.origem JOIN FETCH u.destino " +
                "ORDER BY u.dtSaida DESC", VeiculoUso.class)
                .getResultList();
    }

    public VeiculoUso buscarPorId(UUID id) {
        List<VeiculoUso> result = em.createQuery(
                "SELECT u FROM VeiculoUso u " +
                "JOIN FETCH u.veiculo v JOIN FETCH v.empresa " +
                "JOIN FETCH u.colaborador c JOIN FETCH c.pessoa JOIN FETCH c.empresa " +
                "JOIN FETCH u.origem JOIN FETCH u.destino " +
                "WHERE u.id = :id", VeiculoUso.class)
                .setParameter("id", id)
                .getResultList();
        return result.isEmpty() ? null : result.get(0);
    }

    public boolean existeUsoAtivoVeiculo(UUID veiculoId) {
        Long count = em.createQuery(
                "SELECT COUNT(u) FROM VeiculoUso u WHERE u.veiculo.id = :vid AND u.situacao = 'EM_USO'", Long.class)
                .setParameter("vid", veiculoId)
                .getSingleResult();
        return count > 0;
    }

    public void salvar(VeiculoUso uso) {
        em.persist(uso);
    }

    public void excluir(VeiculoUso uso) {
        em.remove(em.contains(uso) ? uso : em.merge(uso));
    }
}