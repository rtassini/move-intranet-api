package rest;

import java.util.List;
import java.util.UUID;

import jakarta.inject.Inject;
import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.POST;

import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.RestPath;

import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import io.quarkiverse.renarde.Controller;
import model.Local;
import service.LocalService;

public class Locais extends Controller {

    @Inject
    LocalService localService;

    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance lista(List<Local> locais);
        public static native TemplateInstance novo();
        public static native TemplateInstance editar(Local local);
    }

    public TemplateInstance lista() {
        return Templates.lista(localService.listarAtivos());
    }

    public TemplateInstance novo() {
        return Templates.novo();
    }

    @POST
    public void salvar(
            @RestForm @NotBlank String descricao,
            @RestForm String uf) {
        if (validationFailed()) {
            novo();
        }
        localService.criar(descricao, uf);
        lista();
    }

    public TemplateInstance editar(@RestPath UUID id) {
        Local l = localService.buscarPorId(id);
        notFoundIfNull(l);
        return Templates.editar(l);
    }

    @POST
    public void atualizar(
            @RestPath UUID id,
            @RestForm @NotBlank String descricao,
            @RestForm String uf) {
        notFoundIfNull(localService.buscarPorId(id));
        if (validationFailed()) {
            editar(id);
        }
        localService.atualizar(id, descricao, uf);
        lista();
    }

    @POST
    public void excluir(@RestPath UUID id) {
        notFoundIfNull(localService.buscarPorId(id));
        localService.excluir(id);
        lista();
    }
}