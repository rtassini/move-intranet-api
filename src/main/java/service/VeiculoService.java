package service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import model.Empresa;
import model.Veiculo;
import model.VeiculoRodizio;
import repository.EmpresaRepository;
import repository.VeiculoRepository;
import repository.VeiculoRodizioRepository;

@ApplicationScoped
public class VeiculoService {

    @Inject
    VeiculoRepository veiculoRepository;

    @Inject
    EmpresaRepository empresaRepository;

    @Inject
    VeiculoRodizioRepository rodizioRepository;

    public List<Veiculo> listarAtivos() {
        return veiculoRepository.listarAtivos();
    }

    public Veiculo buscarPorId(UUID id) {
        return veiculoRepository.buscarPorId(id);
    }

    @Transactional
    public void criar(UUID empresaId, String placa, String renavam, String chassi, String descricao,
            Short anoFabricacao, Short anoModelo, String cor, String combustivel,
            String proprietario, String cpfCnpj, String situacao) {
        Veiculo v = new Veiculo();
        v.aplicar(placa, renavam, chassi, descricao, anoFabricacao, anoModelo,
                cor, combustivel, proprietario, cpfCnpj, situacao);
        v.empresa = empresaRepository.buscarPorId(empresaId);
        v.rodizio = derivarRodizio(placa);
        veiculoRepository.salvar(v);
    }

    @Transactional
    public void atualizar(UUID id, UUID empresaId, String placa, String renavam, String chassi, String descricao,
            Short anoFabricacao, Short anoModelo, String cor, String combustivel,
            String proprietario, String cpfCnpj, String situacao) {
        Veiculo v = veiculoRepository.buscarPorId(id);
        v.aplicar(placa, renavam, chassi, descricao, anoFabricacao, anoModelo,
                cor, combustivel, proprietario, cpfCnpj, situacao);
        v.empresa = empresaRepository.buscarPorId(empresaId);
        v.rodizio = derivarRodizio(placa);
    }

    @Transactional
    public void excluir(UUID id) {
        Veiculo v = veiculoRepository.buscarPorId(id);
        v.deletedAt = LocalDateTime.now();
    }

    private VeiculoRodizio derivarRodizio(String placa) {
        if (placa == null || placa.isBlank()) return null;
        char ultimo = placa.charAt(placa.length() - 1);
        if (!Character.isDigit(ultimo)) return null;
        short finalPlaca = (short) Character.getNumericValue(ultimo);
        return rodizioRepository.buscarPorFinalPlaca(finalPlaca);
    }
}