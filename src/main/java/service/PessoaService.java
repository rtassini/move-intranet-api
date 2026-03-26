package service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import model.Pessoa;
import repository.PessoaRepository;

@ApplicationScoped
public class PessoaService {

    @Inject
    PessoaRepository pessoaRepository;

    public List<Pessoa> listarAtivas() {
        return pessoaRepository.listarAtivas();
    }

    public Pessoa buscarPorId(UUID id) {
        return pessoaRepository.buscarPorId(id);
    }

    @Transactional
    public UUID criar(String nomeCompleto, String nomeSocial, String sexo,
            LocalDate dataNascimento, String nomeMae, String nomePai, String raca, String religiao,
            String nacionalidade, String naturalidade, String tipoSanguineo, String estadoCivil,
            boolean possuiDependentes, boolean pcd, String tipoDeficiencia, String escolaridade,
            String instituicaoEscolar, LocalDate dataConclusaoEscolar, String email,
            String logradouro, String numero, String complemento, String bairro, String cidade,
            String uf, String cep) {
        Pessoa p = new Pessoa();
        p.aplicar(nomeCompleto, nomeSocial, sexo, dataNascimento, nomeMae, nomePai, raca,
                religiao, nacionalidade, naturalidade, tipoSanguineo, estadoCivil,
                possuiDependentes, pcd, tipoDeficiencia, escolaridade, instituicaoEscolar,
                dataConclusaoEscolar, email, logradouro, numero, complemento, bairro, cidade, uf, cep);
        pessoaRepository.salvar(p);
        return p.id;
    }

    @Transactional
    public void atualizar(UUID id, String nomeCompleto, String nomeSocial, String sexo,
            LocalDate dataNascimento, String nomeMae, String nomePai, String raca, String religiao,
            String nacionalidade, String naturalidade, String tipoSanguineo, String estadoCivil,
            boolean possuiDependentes, boolean pcd, String tipoDeficiencia, String escolaridade,
            String instituicaoEscolar, LocalDate dataConclusaoEscolar, String email,
            String logradouro, String numero, String complemento, String bairro, String cidade,
            String uf, String cep) {
        Pessoa p = pessoaRepository.buscarPorId(id);
        p.aplicar(nomeCompleto, nomeSocial, sexo, dataNascimento, nomeMae, nomePai, raca,
                religiao, nacionalidade, naturalidade, tipoSanguineo, estadoCivil,
                possuiDependentes, pcd, tipoDeficiencia, escolaridade, instituicaoEscolar,
                dataConclusaoEscolar, email, logradouro, numero, complemento, bairro, cidade, uf, cep);
    }

    @Transactional
    public void excluir(UUID id) {
        Pessoa p = pessoaRepository.buscarPorId(id);
        p.deletedAt = LocalDateTime.now();
    }
}