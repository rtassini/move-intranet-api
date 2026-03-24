package service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import model.Empresa;
import repository.EmpresaRepository;

@ApplicationScoped
public class EmpresaService {

    @Inject
    EmpresaRepository empresaRepository;

    public List<Empresa> listarAtivas() {
        return empresaRepository.listarAtivas();
    }

    public Empresa buscarPorId(UUID id) {
        return empresaRepository.buscarPorId(id);
    }

    @Transactional
    public void criar(String razaoSocial, String cnpj, String inscricaoEstadual, String ramoAtividade,
            String status, String logradouro, String numero, String complemento,
            String bairro, String cidade, String uf, String cep) {
        Empresa e = new Empresa();
        e.aplicar(razaoSocial, cnpj, inscricaoEstadual, ramoAtividade, status,
                logradouro, numero, complemento, bairro, cidade, uf, cep);
        empresaRepository.salvar(e);
    }

    @Transactional
    public void atualizar(UUID id, String razaoSocial, String cnpj, String inscricaoEstadual,
            String ramoAtividade, String status, String logradouro, String numero,
            String complemento, String bairro, String cidade, String uf, String cep) {
        Empresa e = empresaRepository.buscarPorId(id);
        e.aplicar(razaoSocial, cnpj, inscricaoEstadual, ramoAtividade, status,
                logradouro, numero, complemento, bairro, cidade, uf, cep);
    }

    @Transactional
    public void excluir(UUID id) {
        Empresa e = empresaRepository.buscarPorId(id);
        e.deletedAt = LocalDateTime.now();
    }
}