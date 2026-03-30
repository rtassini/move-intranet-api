package rest;

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
import model.VeiculoRodizio;
import service.VeiculoRodizioService;

public class VeiculoRodizios extends Controller {

    @Inject
    VeiculoRodizioService rodizioService;

    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance lista(List<VeiculoRodizio> rodizios);
        public static native TemplateInstance novo();
        public static native TemplateInstance editar(VeiculoRodizio rodizio);
    }

    public TemplateInstance lista() {
        return Templates.lista(rodizioService.listarAtivos());
    }

    public TemplateInstance novo() {
        return Templates.novo();
    }

    @POST
    public void salvar(
            @RestForm @NotNull Short finalPlaca,
            @RestForm @NotBlank String descricao) {
        if (validationFailed()) {
            novo();
        }
        rodizioService.criar(finalPlaca, descricao);
        lista();
    }

    public TemplateInstance editar(@RestPath UUID id) {
        VeiculoRodizio r = rodizioService.buscarPorId(id);
        notFoundIfNull(r);
        return Templates.editar(r);
    }

    @POST
    public void atualizar(
            @RestPath UUID id,
            @RestForm @NotNull Short finalPlaca,
            @RestForm @NotBlank String descricao) {
        notFoundIfNull(rodizioService.buscarPorId(id));
        if (validationFailed()) {
            editar(id);
        }
        rodizioService.atualizar(id, finalPlaca, descricao);
        lista();
    }

    @POST
    public void excluir(@RestPath UUID id) {
        notFoundIfNull(rodizioService.buscarPorId(id));
        rodizioService.excluir(id);
        lista();
    }
}