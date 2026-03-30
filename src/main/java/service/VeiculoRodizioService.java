package service;

import java.util.List;
import java.util.UUID;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import model.VeiculoRodizio;
import repository.VeiculoRodizioRepository;

@ApplicationScoped
public class VeiculoRodizioService {

    @Inject
    VeiculoRodizioRepository rodizioRepository;

    public List<VeiculoRodizio> listarAtivos() {
        return rodizioRepository.listarAtivos();
    }

    public VeiculoRodizio buscarPorId(UUID id) {
        return rodizioRepository.buscarPorId(id);
    }

    @Transactional
    public void criar(Short finalPlaca, String descricao) {
        VeiculoRodizio r = new VeiculoRodizio();
        r.aplicar(finalPlaca, descricao);
        rodizioRepository.salvar(r);
    }

    @Transactional
    public void atualizar(UUID id, Short finalPlaca, String descricao) {
        VeiculoRodizio r = rodizioRepository.buscarPorId(id);
        r.aplicar(finalPlaca, descricao);
    }

    @Transactional
    public void excluir(UUID id) {
        VeiculoRodizio r = rodizioRepository.buscarPorId(id);
        r.status = false;
    }
}