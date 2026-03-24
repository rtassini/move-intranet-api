package rest;

import java.time.LocalDate;
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
import model.Pessoa;
import service.PessoaService;

public class Pessoas extends Controller {

    @Inject
    PessoaService pessoaService;

    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance lista(List<Pessoa> pessoas);
        public static native TemplateInstance nova();
        public static native TemplateInstance editar(Pessoa pessoa);
    }

    public TemplateInstance lista() {
        return Templates.lista(pessoaService.listarAtivas());
    }

    public TemplateInstance nova() {
        return Templates.nova();
    }

    @POST
    public void salvar(
            @RestForm @NotBlank String nomeCompleto,
            @RestForm String nomeSocial,
            @RestForm String sexo,
            @RestForm LocalDate dataNascimento,
            @RestForm String nomeMae,
            @RestForm String nomePai,
            @RestForm String raca,
            @RestForm String religiao,
            @RestForm String nacionalidade,
            @RestForm String naturalidade,
            @RestForm String tipoSanguineo,
            @RestForm String estadoCivil,
            @RestForm boolean possuiDependentes,
            @RestForm boolean pcd,
            @RestForm String tipoDeficiencia,
            @RestForm String escolaridade,
            @RestForm String instituicaoEscolar,
            @RestForm LocalDate dataConclusaoEscolar,
            @RestForm String email,
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
        pessoaService.criar(nomeCompleto, nomeSocial, sexo, dataNascimento, nomeMae, nomePai, raca,
                religiao, nacionalidade, naturalidade, tipoSanguineo, estadoCivil,
                possuiDependentes, pcd, tipoDeficiencia, escolaridade, instituicaoEscolar,
                dataConclusaoEscolar, email, logradouro, numero, complemento, bairro, cidade, uf, cep);
        lista();
    }

    public TemplateInstance editar(@RestPath UUID id) {
        Pessoa p = pessoaService.buscarPorId(id);
        notFoundIfNull(p);
        return Templates.editar(p);
    }

    @POST
    public void atualizar(
            @RestPath UUID id,
            @RestForm @NotBlank String nomeCompleto,
            @RestForm String nomeSocial,
            @RestForm String sexo,
            @RestForm LocalDate dataNascimento,
            @RestForm String nomeMae,
            @RestForm String nomePai,
            @RestForm String raca,
            @RestForm String religiao,
            @RestForm String nacionalidade,
            @RestForm String naturalidade,
            @RestForm String tipoSanguineo,
            @RestForm String estadoCivil,
            @RestForm boolean possuiDependentes,
            @RestForm boolean pcd,
            @RestForm String tipoDeficiencia,
            @RestForm String escolaridade,
            @RestForm String instituicaoEscolar,
            @RestForm LocalDate dataConclusaoEscolar,
            @RestForm String email,
            @RestForm String logradouro,
            @RestForm String numero,
            @RestForm String complemento,
            @RestForm String bairro,
            @RestForm String cidade,
            @RestForm String uf,
            @RestForm String cep) {
        notFoundIfNull(pessoaService.buscarPorId(id));
        if (validationFailed()) {
            editar(id);
        }
        pessoaService.atualizar(id, nomeCompleto, nomeSocial, sexo, dataNascimento, nomeMae, nomePai, raca,
                religiao, nacionalidade, naturalidade, tipoSanguineo, estadoCivil,
                possuiDependentes, pcd, tipoDeficiencia, escolaridade, instituicaoEscolar,
                dataConclusaoEscolar, email, logradouro, numero, complemento, bairro, cidade, uf, cep);
        lista();
    }

    @POST
    public void excluir(@RestPath UUID id) {
        notFoundIfNull(pessoaService.buscarPorId(id));
        pessoaService.excluir(id);
        lista();
    }
}