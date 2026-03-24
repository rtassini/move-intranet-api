package rest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.POST;

import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.RestPath;

import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import io.quarkiverse.renarde.Controller;
import model.Pessoa;
import model.PessoaRepository;

public class Pessoas extends Controller {

    @Inject
    PessoaRepository pessoaRepository;

    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance lista(List<Pessoa> pessoas);
        public static native TemplateInstance nova();
        public static native TemplateInstance editar(Pessoa pessoa);
    }

    public TemplateInstance lista() {
        return Templates.lista(pessoaRepository.listarAtivas());
    }

    public TemplateInstance nova() {
        return Templates.nova();
    }

    @POST
    @Transactional
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
        Pessoa p = new Pessoa();
        p.aplicar(nomeCompleto, nomeSocial, sexo, dataNascimento, nomeMae, nomePai, raca,
                religiao, nacionalidade, naturalidade, tipoSanguineo, estadoCivil,
                possuiDependentes, pcd, tipoDeficiencia, escolaridade, instituicaoEscolar,
                dataConclusaoEscolar, email, logradouro, numero, complemento, bairro, cidade, uf, cep);
        pessoaRepository.salvar(p);
        lista();
    }

    public TemplateInstance editar(@RestPath UUID id) {
        Pessoa p = pessoaRepository.buscarPorId(id);
        notFoundIfNull(p);
        return Templates.editar(p);
    }

    @POST
    @Transactional
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
        Pessoa p = pessoaRepository.buscarPorId(id);
        notFoundIfNull(p);
        if (validationFailed()) {
            editar(id);
        }
        p.aplicar(nomeCompleto, nomeSocial, sexo, dataNascimento, nomeMae, nomePai, raca,
                religiao, nacionalidade, naturalidade, tipoSanguineo, estadoCivil,
                possuiDependentes, pcd, tipoDeficiencia, escolaridade, instituicaoEscolar,
                dataConclusaoEscolar, email, logradouro, numero, complemento, bairro, cidade, uf, cep);
        lista();
    }

    @POST
    @Transactional
    public void excluir(@RestPath UUID id) {
        Pessoa p = pessoaRepository.buscarPorId(id);
        notFoundIfNull(p);
        p.deletedAt = LocalDateTime.now();
        lista();
    }
}