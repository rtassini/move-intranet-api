package service;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import model.Pessoa;
import model.PessoaDocumento;
import repository.PessoaDocumentoRepository;
import repository.PessoaRepository;

@ApplicationScoped
public class PessoaDocumentoService {

    @Inject
    PessoaDocumentoRepository docRepository;

    @Inject
    PessoaRepository pessoaRepository;

    public PessoaDocumento buscarPorPessoa(UUID pessoaId) {
        return docRepository.buscarPorPessoa(pessoaId);
    }

    @Transactional
    public void salvar(UUID pessoaId, String cpf, String rg, LocalDate dataExpedicaoRg,
            String orgaoEmissorRg, String pisPasep, String nis, String cns,
            String tituloEleitor, String tituloEleitorZona, String tituloEleitorSecao,
            String reservista, String numeroCtps, String serieCtps,
            LocalDate dataExpedicaoCtps, String ufCtps,
            String numeroCnh, String categoriaCnh, LocalDate validadeCnh) {
        PessoaDocumento doc = docRepository.buscarPorPessoa(pessoaId);
        if (doc == null) {
            Pessoa pessoa = pessoaRepository.buscarPorId(pessoaId);
            doc = new PessoaDocumento();
            doc.pessoa = pessoa;
            doc.aplicar(cpf, rg, dataExpedicaoRg, orgaoEmissorRg, pisPasep, nis, cns,
                    tituloEleitor, tituloEleitorZona, tituloEleitorSecao, reservista,
                    numeroCtps, serieCtps, dataExpedicaoCtps, ufCtps,
                    numeroCnh, categoriaCnh, validadeCnh);
            docRepository.salvar(doc);
        } else {
            doc.aplicar(cpf, rg, dataExpedicaoRg, orgaoEmissorRg, pisPasep, nis, cns,
                    tituloEleitor, tituloEleitorZona, tituloEleitorSecao, reservista,
                    numeroCtps, serieCtps, dataExpedicaoCtps, ufCtps,
                    numeroCnh, categoriaCnh, validadeCnh);
        }
    }
}