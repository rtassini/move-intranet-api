package service;

import java.util.List;
import java.util.UUID;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import model.Pessoa;
import model.Telefone;
import repository.PessoaRepository;
import repository.TelefoneRepository;

@ApplicationScoped
public class TelefoneService {

    @Inject
    TelefoneRepository telefoneRepository;

    @Inject
    PessoaRepository pessoaRepository;

    public List<Telefone> listarPorPessoa(UUID pessoaId) {
        return telefoneRepository.listarPorPessoa(pessoaId);
    }

    @Transactional
    public void adicionarParaPessoa(UUID pessoaId, String numero, String tipo) {
        Pessoa pessoa = pessoaRepository.buscarPorId(pessoaId);
        Telefone telefone = new Telefone();
        telefone.pessoa = pessoa;
        telefone.numero = numero;
        telefone.tipo = tipo;
        telefoneRepository.salvar(telefone);
    }

    @Transactional
    public void excluir(UUID id) {
        Telefone t = telefoneRepository.buscarPorId(id);
        if (t != null) {
            telefoneRepository.remover(t);
        }
    }
}