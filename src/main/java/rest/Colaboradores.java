package rest;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import jakarta.inject.Inject;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.POST;

import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.RestPath;

import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import io.quarkiverse.renarde.Controller;
import model.Colaborador;
import model.Empresa;
import model.Pessoa;
import repository.EmpresaRepository;
import repository.PessoaRepository;
import service.ColaboradorService;

public class Colaboradores extends Controller {

    @Inject
    ColaboradorService colaboradorService;

    @Inject
    PessoaRepository pessoaRepository;

    @Inject
    EmpresaRepository empresaRepository;

    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance lista(List<Colaborador> colaboradores);
        public static native TemplateInstance novo(List<Pessoa> pessoas, List<Empresa> empresas);
        public static native TemplateInstance editar(Colaborador colaborador, List<Pessoa> pessoas, List<Empresa> empresas);
    }

    public TemplateInstance lista() {
        return Templates.lista(colaboradorService.listarAtivos());
    }

    public TemplateInstance novo() {
        return Templates.novo(pessoaRepository.listarAtivas(), empresaRepository.listarAtivas());
    }

    @POST
    public void salvar(
            @RestForm @NotNull UUID pessoaId,
            @RestForm @NotNull UUID empresaId,
            @RestForm String cargo,
            @RestForm boolean responsavel,
            @RestForm @NotNull LocalDate dataInicio) {
        if (validationFailed()) {
            novo();
        }
        colaboradorService.criar(pessoaId, empresaId, cargo, responsavel, dataInicio);
        lista();
    }

    public TemplateInstance editar(@RestPath UUID id) {
        Colaborador c = colaboradorService.buscarPorId(id);
        notFoundIfNull(c);
        return Templates.editar(c, pessoaRepository.listarAtivas(), empresaRepository.listarAtivas());
    }

    @POST
    public void atualizar(
            @RestPath UUID id,
            @RestForm @NotNull UUID pessoaId,
            @RestForm @NotNull UUID empresaId,
            @RestForm String cargo,
            @RestForm boolean responsavel,
            @RestForm @NotNull LocalDate dataInicio) {
        notFoundIfNull(colaboradorService.buscarPorId(id));
        if (validationFailed()) {
            editar(id);
        }
        colaboradorService.atualizar(id, pessoaId, empresaId, cargo, responsavel, dataInicio);
        lista();
    }

    @POST
    public void excluir(@RestPath UUID id) {
        notFoundIfNull(colaboradorService.buscarPorId(id));
        colaboradorService.excluir(id);
        lista();
    }
}