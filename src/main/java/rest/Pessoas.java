package rest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.POST;

import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.RestPath;

import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import io.quarkiverse.renarde.Controller;
import model.Pessoa;

public class Pessoas extends Controller {

    @Inject
    EntityManager em;

    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance lista(List<Pessoa> pessoas);
        public static native TemplateInstance nova();
        public static native TemplateInstance editar(Pessoa pessoa);
    }

    public TemplateInstance lista() {
        List<Pessoa> pessoas = em.createQuery(
                "FROM Pessoa p WHERE p.deletedAt IS NULL ORDER BY p.nomeCompleto", Pessoa.class)
                .getResultList();
        return Templates.lista(pessoas);
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
        preencher(p, nomeCompleto, nomeSocial, sexo, dataNascimento, nomeMae, nomePai, raca,
                religiao, nacionalidade, naturalidade, tipoSanguineo, estadoCivil,
                possuiDependentes, pcd, tipoDeficiencia, escolaridade, instituicaoEscolar,
                dataConclusaoEscolar, email, logradouro, numero, complemento, bairro, cidade, uf, cep);
        em.persist(p);
        lista();
    }

    public TemplateInstance editar(@RestPath UUID id) {
        Pessoa p = em.find(Pessoa.class, id);
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
        Pessoa p = em.find(Pessoa.class, id);
        notFoundIfNull(p);
        if (validationFailed()) {
            editar(id);
        }
        preencher(p, nomeCompleto, nomeSocial, sexo, dataNascimento, nomeMae, nomePai, raca,
                religiao, nacionalidade, naturalidade, tipoSanguineo, estadoCivil,
                possuiDependentes, pcd, tipoDeficiencia, escolaridade, instituicaoEscolar,
                dataConclusaoEscolar, email, logradouro, numero, complemento, bairro, cidade, uf, cep);
        lista();
    }

    @POST
    @Transactional
    public void excluir(@RestPath UUID id) {
        Pessoa p = em.find(Pessoa.class, id);
        notFoundIfNull(p);
        p.deletedAt = LocalDateTime.now();
        lista();
    }

    private void preencher(Pessoa p, String nomeCompleto, String nomeSocial, String sexo,
            LocalDate dataNascimento, String nomeMae, String nomePai, String raca, String religiao,
            String nacionalidade, String naturalidade, String tipoSanguineo, String estadoCivil,
            boolean possuiDependentes, boolean pcd, String tipoDeficiencia, String escolaridade,
            String instituicaoEscolar, LocalDate dataConclusaoEscolar, String email,
            String logradouro, String numero, String complemento, String bairro, String cidade,
            String uf, String cep) {
        p.nomeCompleto = nomeCompleto;
        p.nomeSocial = nomeSocial;
        p.sexo = sexo;
        p.dataNascimento = dataNascimento;
        p.nomeMae = nomeMae;
        p.nomePai = nomePai;
        p.raca = raca;
        p.religiao = religiao;
        p.nacionalidade = nacionalidade;
        p.naturalidade = naturalidade;
        p.tipoSanguineo = tipoSanguineo;
        p.estadoCivil = estadoCivil;
        p.possuiDependentes = possuiDependentes;
        p.pcd = pcd;
        p.tipoDeficiencia = tipoDeficiencia;
        p.escolaridade = escolaridade;
        p.instituicaoEscolar = instituicaoEscolar;
        p.dataConclusaoEscolar = dataConclusaoEscolar;
        p.email = email;
        p.logradouro = logradouro;
        p.numero = numero;
        p.complemento = complemento;
        p.bairro = bairro;
        p.cidade = cidade;
        p.uf = uf;
        p.cep = cep;
    }
}