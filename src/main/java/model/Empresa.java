package model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "empresas")
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID")
    public UUID id;

    @Column(name = "razao_social", nullable = false, length = 200)
    public String razaoSocial;

    @Column(nullable = false, length = 18)
    public String cnpj;

    @Column(name = "inscricao_estadual", length = 20)
    public String inscricaoEstadual;

    @Column(name = "ramo_atividade", length = 100)
    public String ramoAtividade;

    @Column(length = 20)
    public String status = "ATIVO";

    @Column(length = 200)
    public String logradouro;

    @Column(length = 20)
    public String numero;

    @Column(length = 100)
    public String complemento;

    @Column(length = 100)
    public String bairro;

    @Column(length = 100)
    public String cidade;

    @Column(length = 2)
    public String uf;

    @Column(length = 9)
    public String cep;

    @Column(name = "data_cadastro", updatable = false)
    public LocalDateTime dataCadastro;

    @Column(name = "created_at", updatable = false)
    public LocalDateTime createdAt;

    @Column(name = "updated_at")
    public LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    public LocalDateTime deletedAt;

    public void aplicar(String razaoSocial, String cnpj, String inscricaoEstadual, String ramoAtividade,
            String status, String logradouro, String numero, String complemento,
            String bairro, String cidade, String uf, String cep) {
        this.razaoSocial = razaoSocial;
        this.cnpj = cnpj;
        this.inscricaoEstadual = inscricaoEstadual;
        this.ramoAtividade = ramoAtividade;
        this.status = status;
        this.logradouro = logradouro;
        this.numero = numero;
        this.complemento = complemento;
        this.bairro = bairro;
        this.cidade = cidade;
        this.uf = uf;
        this.cep = cep;
    }

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        dataCadastro = now;
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}