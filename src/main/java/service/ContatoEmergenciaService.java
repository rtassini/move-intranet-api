package service;

import java.util.List;
import java.util.UUID;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import model.ContatoEmergencia;
import model.Pessoa;
import repository.ContatoEmergenciaRepository;
import repository.PessoaRepository;

@ApplicationScoped
public class ContatoEmergenciaService {

    @Inject
    ContatoEmergenciaRepository contatoRepository;

    @Inject
    PessoaRepository pessoaRepository;

    public List<ContatoEmergencia> listarPorPessoa(UUID pessoaId) {
        return contatoRepository.listarPorPessoa(pessoaId);
    }

    @Transactional
    public void adicionar(UUID pessoaId, String nome, String parentesco, String telefone) {
        Pessoa pessoa = pessoaRepository.buscarPorId(pessoaId);
        ContatoEmergencia contato = new ContatoEmergencia();
        contato.pessoa = pessoa;
        contato.nome = nome;
        contato.parentesco = parentesco;
        contato.telefone = telefone;
        contatoRepository.salvar(contato);
    }

    @Transactional
    public void excluir(UUID id) {
        ContatoEmergencia c = contatoRepository.buscarPorId(id);
        if (c != null) {
            contatoRepository.remover(c);
        }
    }
}