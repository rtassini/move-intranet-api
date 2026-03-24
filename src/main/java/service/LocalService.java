package service;

import java.util.List;
import java.util.UUID;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import model.Local;
import repository.LocalRepository;

@ApplicationScoped
public class LocalService {

    @Inject
    LocalRepository localRepository;

    public List<Local> listarAtivos() {
        return localRepository.listarAtivos();
    }

    public Local buscarPorId(UUID id) {
        return localRepository.buscarPorId(id);
    }

    @Transactional
    public void criar(String descricao, String uf) {
        Local l = new Local();
        l.aplicar(descricao, uf);
        localRepository.salvar(l);
    }

    @Transactional
    public void atualizar(UUID id, String descricao, String uf) {
        Local l = localRepository.buscarPorId(id);
        l.aplicar(descricao, uf);
    }

    @Transactional
    public void excluir(UUID id) {
        Local l = localRepository.buscarPorId(id);
        l.status = false;
    }
}