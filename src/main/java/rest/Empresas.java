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
import model.Empresa;
import service.EmpresaService;

public class Empresas extends Controller {

    @Inject
    EmpresaService empresaService;

    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance lista(List<Empresa> empresas);
        public static native TemplateInstance nova();
        public static native TemplateInstance editar(Empresa empresa);
    }

    public TemplateInstance lista() {
        return Templates.lista(empresaService.listarAtivas());
    }

    public TemplateInstance nova() {
        return Templates.nova();
    }

    @POST
    public void salvar(
            @RestForm @NotBlank String razaoSocial,
            @RestForm @NotBlank String cnpj,
            @RestForm String inscricaoEstadual,
            @RestForm String ramoAtividade,
            @RestForm String status,
            @RestForm String logradouro,
            @RestForm String numero,
            @RestForm String complemento,
            @RestForm String bairro,
            @RestForm String cidade,
            @RestForm String uf,
            @RestForm String cep) {
        if (validationFailed()) {
            nova();
        }
        empresaService.criar(razaoSocial, cnpj, inscricaoEstadual, ramoAtividade,
                status != null ? status : "ATIVO",
                logradouro, numero, complemento, bairro, cidade, uf, cep);
        lista();
    }

    public TemplateInstance editar(@RestPath UUID id) {
        Empresa e = empresaService.buscarPorId(id);
        notFoundIfNull(e);
        return Templates.editar(e);
    }

    @POST
    public void atualizar(
            @RestPath UUID id,
            @RestForm @NotBlank String razaoSocial,
            @RestForm @NotBlank String cnpj,
            @RestForm String inscricaoEstadual,
            @RestForm String ramoAtividade,
            @RestForm String status,
            @RestForm String logradouro,
            @RestForm String numero,
            @RestForm String complemento,
            @RestForm String bairro,
            @RestForm String cidade,
            @RestForm String uf,
            @RestForm String cep) {
        notFoundIfNull(empresaService.buscarPorId(id));
        if (validationFailed()) {
            editar(id);
        }
        empresaService.atualizar(id, razaoSocial, cnpj, inscricaoEstadual, ramoAtividade,
                status != null ? status : "ATIVO",
                logradouro, numero, complemento, bairro, cidade, uf, cep);
        lista();
    }

    @POST
    public void excluir(@RestPath UUID id) {
        notFoundIfNull(empresaService.buscarPorId(id));
        empresaService.excluir(id);
        lista();
    }
}