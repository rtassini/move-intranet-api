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
import model.ContatoEmergencia;
import model.Pessoa;
import model.PessoaDocumento;
import model.Telefone;
import service.ContatoEmergenciaService;
import service.PessoaDocumentoService;
import service.PessoaService;
import service.TelefoneService;

public class Pessoas extends Controller {

    @Inject
    PessoaService pessoaService;

    @Inject
    PessoaDocumentoService documentoService;

    @Inject
    TelefoneService telefoneService;

    @Inject
    ContatoEmergenciaService contatoEmergenciaService;

    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance lista(List<Pessoa> pessoas);
        public static native TemplateInstance nova();
        public static native TemplateInstance editar(Pessoa pessoa);
        public static native TemplateInstance documentos(Pessoa pessoa, PessoaDocumento doc);
        public static native TemplateInstance telefones(Pessoa pessoa, List<Telefone> telefones);
        public static native TemplateInstance contatosEmergencia(Pessoa pessoa, List<ContatoEmergencia> contatos);
        public static native TemplateInstance editarDocumentos(Pessoa pessoa, PessoaDocumento doc);
        public static native TemplateInstance editarTelefones(Pessoa pessoa, List<Telefone> telefones);
        public static native TemplateInstance editarContatosEmergencia(Pessoa pessoa, List<ContatoEmergencia> contatos);
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
        UUID pessoaId = pessoaService.criar(nomeCompleto, nomeSocial, sexo, dataNascimento, nomeMae, nomePai, raca,
                religiao, nacionalidade, naturalidade, tipoSanguineo, estadoCivil,
                possuiDependentes, pcd, tipoDeficiencia, escolaridade, instituicaoEscolar,
                dataConclusaoEscolar, email, logradouro, numero, complemento, bairro, cidade, uf, cep);
        documentos(pessoaId);
    }

    public TemplateInstance documentos(@RestPath UUID pessoaId) {
        Pessoa pessoa = pessoaService.buscarPorId(pessoaId);
        notFoundIfNull(pessoa);
        PessoaDocumento doc = documentoService.buscarPorPessoa(pessoaId);
        if (doc == null) doc = new PessoaDocumento();
        return Templates.documentos(pessoa, doc);
    }

    @POST
    public void salvarDocumentos(
            @RestPath UUID pessoaId,
            @RestForm @NotBlank String cpf,
            @RestForm String rg,
            @RestForm LocalDate dataExpedicaoRg,
            @RestForm String orgaoEmissorRg,
            @RestForm String pisPasep,
            @RestForm String nis,
            @RestForm String cns,
            @RestForm String tituloEleitor,
            @RestForm String tituloEleitorZona,
            @RestForm String tituloEleitorSecao,
            @RestForm String reservista,
            @RestForm String numeroCtps,
            @RestForm String serieCtps,
            @RestForm LocalDate dataExpedicaoCtps,
            @RestForm String ufCtps,
            @RestForm String numeroCnh,
            @RestForm String categoriaCnh,
            @RestForm LocalDate validadeCnh) {
        notFoundIfNull(pessoaService.buscarPorId(pessoaId));
        if (validationFailed()) {
            documentos(pessoaId);
        }
        documentoService.salvar(pessoaId, cpf, rg, dataExpedicaoRg, orgaoEmissorRg, pisPasep, nis, cns,
                tituloEleitor, tituloEleitorZona, tituloEleitorSecao, reservista,
                numeroCtps, serieCtps, dataExpedicaoCtps, ufCtps,
                numeroCnh, categoriaCnh, validadeCnh);
        telefones(pessoaId);
    }

    public TemplateInstance telefones(@RestPath UUID pessoaId) {
        Pessoa pessoa = pessoaService.buscarPorId(pessoaId);
        notFoundIfNull(pessoa);
        return Templates.telefones(pessoa, telefoneService.listarPorPessoa(pessoaId));
    }

    @POST
    public void adicionarTelefone(
            @RestPath UUID pessoaId,
            @RestForm @NotBlank String numero,
            @RestForm String tipo) {
        notFoundIfNull(pessoaService.buscarPorId(pessoaId));
        if (validationFailed()) {
            telefones(pessoaId);
        }
        telefoneService.adicionarParaPessoa(pessoaId, numero, tipo);
        telefones(pessoaId);
    }

    @POST
    public void excluirTelefone(@RestPath UUID pessoaId, @RestPath UUID telefoneId) {
        notFoundIfNull(pessoaService.buscarPorId(pessoaId));
        telefoneService.excluir(telefoneId);
        telefones(pessoaId);
    }

    @POST
    public void concluirTelefones(@RestPath UUID pessoaId) {
        contatosEmergencia(pessoaId);
    }

    public TemplateInstance contatosEmergencia(@RestPath UUID pessoaId) {
        Pessoa pessoa = pessoaService.buscarPorId(pessoaId);
        notFoundIfNull(pessoa);
        return Templates.contatosEmergencia(pessoa, contatoEmergenciaService.listarPorPessoa(pessoaId));
    }

    @POST
    public void adicionarContatoEmergencia(
            @RestPath UUID pessoaId,
            @RestForm @NotBlank String nome,
            @RestForm String parentesco,
            @RestForm @NotBlank String telefone) {
        notFoundIfNull(pessoaService.buscarPorId(pessoaId));
        if (validationFailed()) {
            contatosEmergencia(pessoaId);
        }
        contatoEmergenciaService.adicionar(pessoaId, nome, parentesco, telefone);
        contatosEmergencia(pessoaId);
    }

    @POST
    public void excluirContatoEmergencia(@RestPath UUID pessoaId, @RestPath UUID contatoId) {
        notFoundIfNull(pessoaService.buscarPorId(pessoaId));
        contatoEmergenciaService.excluir(contatoId);
        contatosEmergencia(pessoaId);
    }

    @POST
    public void concluirContatosEmergencia(@RestPath UUID pessoaId) {
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

    public TemplateInstance editarDocumentos(@RestPath UUID id) {
        Pessoa pessoa = pessoaService.buscarPorId(id);
        notFoundIfNull(pessoa);
        PessoaDocumento doc = documentoService.buscarPorPessoa(id);
        if (doc == null) doc = new PessoaDocumento();
        return Templates.editarDocumentos(pessoa, doc);
    }

    @POST
    public void atualizarDocumentos(
            @RestPath UUID id,
            @RestForm @NotBlank String cpf,
            @RestForm String rg,
            @RestForm LocalDate dataExpedicaoRg,
            @RestForm String orgaoEmissorRg,
            @RestForm String pisPasep,
            @RestForm String nis,
            @RestForm String cns,
            @RestForm String tituloEleitor,
            @RestForm String tituloEleitorZona,
            @RestForm String tituloEleitorSecao,
            @RestForm String reservista,
            @RestForm String numeroCtps,
            @RestForm String serieCtps,
            @RestForm LocalDate dataExpedicaoCtps,
            @RestForm String ufCtps,
            @RestForm String numeroCnh,
            @RestForm String categoriaCnh,
            @RestForm LocalDate validadeCnh) {
        notFoundIfNull(pessoaService.buscarPorId(id));
        if (validationFailed()) {
            editarDocumentos(id);
        }
        documentoService.salvar(id, cpf, rg, dataExpedicaoRg, orgaoEmissorRg, pisPasep, nis, cns,
                tituloEleitor, tituloEleitorZona, tituloEleitorSecao, reservista,
                numeroCtps, serieCtps, dataExpedicaoCtps, ufCtps,
                numeroCnh, categoriaCnh, validadeCnh);
        lista();
    }

    public TemplateInstance editarTelefones(@RestPath UUID id) {
        Pessoa pessoa = pessoaService.buscarPorId(id);
        notFoundIfNull(pessoa);
        return Templates.editarTelefones(pessoa, telefoneService.listarPorPessoa(id));
    }

    @POST
    public void adicionarTelefoneEdicao(
            @RestPath UUID id,
            @RestForm @NotBlank String numero,
            @RestForm String tipo) {
        notFoundIfNull(pessoaService.buscarPorId(id));
        if (validationFailed()) {
            editarTelefones(id);
        }
        telefoneService.adicionarParaPessoa(id, numero, tipo);
        editarTelefones(id);
    }

    @POST
    public void excluirTelefoneEdicao(@RestPath UUID id, @RestPath UUID telefoneId) {
        notFoundIfNull(pessoaService.buscarPorId(id));
        telefoneService.excluir(telefoneId);
        editarTelefones(id);
    }

    public TemplateInstance editarContatosEmergencia(@RestPath UUID id) {
        Pessoa pessoa = pessoaService.buscarPorId(id);
        notFoundIfNull(pessoa);
        return Templates.editarContatosEmergencia(pessoa, contatoEmergenciaService.listarPorPessoa(id));
    }

    @POST
    public void adicionarContatoEmergenciaEdicao(
            @RestPath UUID id,
            @RestForm @NotBlank String nome,
            @RestForm String parentesco,
            @RestForm @NotBlank String telefone) {
        notFoundIfNull(pessoaService.buscarPorId(id));
        if (validationFailed()) {
            editarContatosEmergencia(id);
        }
        contatoEmergenciaService.adicionar(id, nome, parentesco, telefone);
        editarContatosEmergencia(id);
    }

    @POST
    public void excluirContatoEmergenciaEdicao(@RestPath UUID id, @RestPath UUID contatoId) {
        notFoundIfNull(pessoaService.buscarPorId(id));
        contatoEmergenciaService.excluir(contatoId);
        editarContatosEmergencia(id);
    }

    @POST
    public void excluir(@RestPath UUID id) {
        notFoundIfNull(pessoaService.buscarPorId(id));
        pessoaService.excluir(id);
        lista();
    }
}