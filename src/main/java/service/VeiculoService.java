package service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import model.Veiculo;
import repository.VeiculoRepository;

@ApplicationScoped
public class VeiculoService {

    @Inject
    VeiculoRepository veiculoRepository;

    public List<Veiculo> listarAtivos() {
        return veiculoRepository.listarAtivos();
    }

    public Veiculo buscarPorId(UUID id) {
        return veiculoRepository.buscarPorId(id);
    }

    @Transactional
    public void criar(String placa, String renavam, String chassi, String descricao,
            Short anoFabricacao, Short anoModelo, String cor, String combustivel,
            String proprietario, String cpfCnpj, String situacao) {
        Veiculo v = new Veiculo();
        v.aplicar(placa, renavam, chassi, descricao, anoFabricacao, anoModelo,
                cor, combustivel, proprietario, cpfCnpj, situacao);
        veiculoRepository.salvar(v);
    }

    @Transactional
    public void atualizar(UUID id, String placa, String renavam, String chassi, String descricao,
            Short anoFabricacao, Short anoModelo, String cor, String combustivel,
            String proprietario, String cpfCnpj, String situacao) {
        Veiculo v = veiculoRepository.buscarPorId(id);
        v.aplicar(placa, renavam, chassi, descricao, anoFabricacao, anoModelo,
                cor, combustivel, proprietario, cpfCnpj, situacao);
    }

    @Transactional
    public void excluir(UUID id) {
        Veiculo v = veiculoRepository.buscarPorId(id);
        v.deletedAt = LocalDateTime.now();
    }
}