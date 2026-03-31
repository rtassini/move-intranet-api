package service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import model.Colaborador;
import model.Local;
import model.Veiculo;
import model.VeiculoUso;
import repository.ColaboradorRepository;
import repository.LocalRepository;
import repository.VeiculoRepository;
import repository.VeiculoUsoRepository;

@ApplicationScoped
public class VeiculoUsoService {

    @Inject
    VeiculoUsoRepository usoRepository;

    @Inject
    VeiculoRepository veiculoRepository;

    @Inject
    ColaboradorRepository colaboradorRepository;

    @Inject
    LocalRepository localRepository;

    public List<VeiculoUso> listarTodos() {
        return usoRepository.listarTodos();
    }

    public VeiculoUso buscarPorId(UUID id) {
        return usoRepository.buscarPorId(id);
    }

    @Transactional
    public void criar(UUID veiculoId, UUID colaboradorId, Integer kmInicial,
            UUID origemId, UUID destinoId, LocalDateTime dtSaida, String observacoes) {

        Veiculo veiculo = veiculoRepository.buscarPorId(veiculoId);
        Colaborador colaborador = colaboradorRepository.buscarPorId(colaboradorId);

        if (!veiculo.empresa.id.equals(colaborador.empresa.id)) {
            throw new IllegalArgumentException(
                    "O veículo pertence a outra empresa que não a do colaborador selecionado.");
        }

        if (!"DISPONIVEL".equals(veiculo.situacao)) {
            throw new IllegalArgumentException(
                    "Veículo não está disponível. Situação atual: " + veiculo.situacao + ".");
        }

        if (veiculo.rodizio != null && veiculo.rodizio.status) {
            DayOfWeek diaRodizio = mapDiaSemana(veiculo.rodizio.descricao);
            if (diaRodizio != null && dtSaida.getDayOfWeek() == diaRodizio) {
                throw new IllegalArgumentException(
                        "Veículo com placa final " + veiculo.rodizio.finalPlaca +
                        " não pode circular na " + veiculo.rodizio.descricao + " (rodízio municipal).");
            }
        }

        Local origem = localRepository.buscarPorId(origemId);
        Local destino = localRepository.buscarPorId(destinoId);

        VeiculoUso uso = new VeiculoUso();
        uso.veiculo = veiculo;
        uso.colaborador = colaborador;
        uso.kmInicial = kmInicial;
        uso.origem = origem;
        uso.destino = destino;
        uso.dtSaida = dtSaida;
        uso.observacoes = observacoes;
        uso.situacao = "EM_USO";

        veiculo.situacao = "EM_USO";

        usoRepository.salvar(uso);
    }

    @Transactional
    public void concluir(UUID id, Integer kmFinal, LocalDateTime dtChegada, String observacoes) {
        VeiculoUso uso = usoRepository.buscarPorId(id);

        if (!"EM_USO".equals(uso.situacao)) {
            throw new IllegalArgumentException("Somente registros em andamento podem ser concluídos.");
        }

        if (kmFinal != null && uso.kmInicial != null && kmFinal < uso.kmInicial) {
            throw new IllegalArgumentException(
                    "KM final (" + kmFinal + ") não pode ser menor que KM inicial (" + uso.kmInicial + ").");
        }

        if (dtChegada != null && dtChegada.isBefore(uso.dtSaida)) {
            throw new IllegalArgumentException("Data/hora de chegada não pode ser anterior à saída.");
        }

        uso.kmFinal = kmFinal;
        uso.dtChegada = dtChegada;
        uso.observacoes = observacoes;
        uso.situacao = "CONCLUIDO";

        uso.veiculo.situacao = "DISPONIVEL";
    }

    @Transactional
    public void cancelar(UUID id) {
        VeiculoUso uso = usoRepository.buscarPorId(id);

        if (!"EM_USO".equals(uso.situacao)) {
            throw new IllegalArgumentException("Somente registros em andamento podem ser cancelados.");
        }

        uso.situacao = "CANCELADO";
        uso.veiculo.situacao = "DISPONIVEL";
    }

    @Transactional
    public void excluir(UUID id) {
        VeiculoUso uso = usoRepository.buscarPorId(id);

        if ("EM_USO".equals(uso.situacao)) {
            throw new IllegalArgumentException("Cancele o uso antes de excluir.");
        }

        usoRepository.excluir(uso);
    }

    private DayOfWeek mapDiaSemana(String descricao) {
        if (descricao == null) return null;
        return switch (descricao.toUpperCase().trim()) {
            case "SEGUNDA-FEIRA" -> DayOfWeek.MONDAY;
            case "TERÇA-FEIRA", "TERCA-FEIRA" -> DayOfWeek.TUESDAY;
            case "QUARTA-FEIRA" -> DayOfWeek.WEDNESDAY;
            case "QUINTA-FEIRA" -> DayOfWeek.THURSDAY;
            case "SEXTA-FEIRA" -> DayOfWeek.FRIDAY;
            case "SÁBADO", "SABADO" -> DayOfWeek.SATURDAY;
            case "DOMINGO" -> DayOfWeek.SUNDAY;
            default -> null;
        };
    }
}
