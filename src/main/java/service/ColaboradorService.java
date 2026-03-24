package service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import model.Colaborador;
import model.Empresa;
import model.Pessoa;
import repository.ColaboradorRepository;
import repository.EmpresaRepository;
import repository.PessoaRepository;

@ApplicationScoped
public class ColaboradorService {

    @Inject
    ColaboradorRepository colaboradorRepository;

    @Inject
    PessoaRepository pessoaRepository;

    @Inject
    EmpresaRepository empresaRepository;

    public List<Colaborador> listarAtivos() {
        return colaboradorRepository.listarAtivos();
    }

    public Colaborador buscarPorId(UUID id) {
        return colaboradorRepository.buscarPorId(id);
    }

    @Transactional
    public void criar(UUID pessoaId, UUID empresaId, String cargo, boolean responsavel, LocalDate dataInicio) {
        Pessoa pessoa = pessoaRepository.buscarPorId(pessoaId);
        Empresa empresa = empresaRepository.buscarPorId(empresaId);

        Colaborador c = new Colaborador();
        c.pessoa = pessoa;
        c.empresa = empresa;
        c.cargo = cargo;
        c.responsavel = responsavel;
        c.dataInicio = dataInicio;

        colaboradorRepository.salvar(c);
    }

    @Transactional
    public void atualizar(UUID id, UUID pessoaId, UUID empresaId, String cargo, boolean responsavel, LocalDate dataInicio) {
        Colaborador c = colaboradorRepository.buscarPorId(id);
        c.pessoa = pessoaRepository.buscarPorId(pessoaId);
        c.empresa = empresaRepository.buscarPorId(empresaId);
        c.cargo = cargo;
        c.responsavel = responsavel;
        c.dataInicio = dataInicio;
    }

    @Transactional
    public void excluir(UUID id) {
        Colaborador c = colaboradorRepository.buscarPorId(id);
        c.deletedAt = LocalDateTime.now();
    }
}