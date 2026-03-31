package rest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.UUID;

import jakarta.inject.Inject;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.POST;

import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.RestPath;

import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import io.quarkiverse.renarde.Controller;
import model.Colaborador;
import model.Local;
import model.Veiculo;
import model.VeiculoUso;
import repository.ColaboradorRepository;
import repository.LocalRepository;
import repository.VeiculoRepository;
import service.VeiculoUsoService;

public class VeiculoUsos extends Controller {

    private static final DateTimeFormatter DT_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    @Inject
    VeiculoUsoService veiculoUsoService;

    @Inject
    VeiculoRepository veiculoRepository;

    @Inject
    ColaboradorRepository colaboradorRepository;

    @Inject
    LocalRepository localRepository;

    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance lista(List<VeiculoUso> usos);
        public static native TemplateInstance novo(List<Veiculo> veiculos, List<Colaborador> colaboradores, List<Local> locais);
        public static native TemplateInstance editar(VeiculoUso uso);
    }

    public TemplateInstance lista() {
        return Templates.lista(veiculoUsoService.listarTodos());
    }

    public TemplateInstance novo() {
        return Templates.novo(
                veiculoRepository.listarDisponiveis(),
                colaboradorRepository.listarAtivos(),
                localRepository.listarAtivos());
    }

    @POST
    public void salvar(
            @RestForm @NotNull UUID veiculoId,
            @RestForm @NotNull UUID colaboradorId,
            @RestForm Integer kmInicial,
            @RestForm @NotNull UUID origemId,
            @RestForm @NotNull UUID destinoId,
            @RestForm @NotBlank String dtSaida,
            @RestForm String observacoes) {
        if (validationFailed()) {
            novo();
            return;
        }
        LocalDateTime dtSaidaParsed;
        try {
            dtSaidaParsed = LocalDateTime.parse(dtSaida, DT_FORMAT);
        } catch (DateTimeParseException e) {
            flash("error", "Data/hora de saída inválida.");
            novo();
            return;
        }
        try {
            veiculoUsoService.criar(veiculoId, colaboradorId, kmInicial, origemId, destinoId, dtSaidaParsed, observacoes);
        } catch (IllegalArgumentException e) {
            flash("error", e.getMessage());
            novo();
            return;
        }
        lista();
    }

    public TemplateInstance editar(@RestPath UUID id) {
        VeiculoUso uso = veiculoUsoService.buscarPorId(id);
        notFoundIfNull(uso);
        if (!"EM_USO".equals(uso.situacao)) {
            lista();
            return null;
        }
        return Templates.editar(uso);
    }

    @POST
    public void concluir(
            @RestPath UUID id,
            @RestForm Integer kmFinal,
            @RestForm String dtChegada,
            @RestForm String observacoes) {
        notFoundIfNull(veiculoUsoService.buscarPorId(id));
        LocalDateTime dtChegadaParsed = null;
        if (dtChegada != null && !dtChegada.isBlank()) {
            try {
                dtChegadaParsed = LocalDateTime.parse(dtChegada, DT_FORMAT);
            } catch (DateTimeParseException e) {
                flash("error", "Data/hora de chegada inválida.");
                editar(id);
                return;
            }
        }
        try {
            veiculoUsoService.concluir(id, kmFinal, dtChegadaParsed, observacoes);
        } catch (IllegalArgumentException e) {
            flash("error", e.getMessage());
            editar(id);
            return;
        }
        lista();
    }

    @POST
    public void cancelar(@RestPath UUID id) {
        notFoundIfNull(veiculoUsoService.buscarPorId(id));
        try {
            veiculoUsoService.cancelar(id);
        } catch (IllegalArgumentException e) {
            flash("error", e.getMessage());
        }
        lista();
    }

    @POST
    public void excluir(@RestPath UUID id) {
        notFoundIfNull(veiculoUsoService.buscarPorId(id));
        try {
            veiculoUsoService.excluir(id);
        } catch (IllegalArgumentException e) {
            flash("error", e.getMessage());
        }
        lista();
    }
}
