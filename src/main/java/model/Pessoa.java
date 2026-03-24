package model;

import java.time.LocalDate;
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
@Table(name = "pessoas")
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID")
    public UUID id;

    @Column(name = "nome_completo", nullable = false, length = 200)
    public String nomeCompleto;

    @Column(name = "nome_social", length = 200)
    public String nomeSocial;

    @Column(length = 20)
    public String sexo;

    @Column(name = "data_nascimento")
    public LocalDate dataNascimento;

    @Column(name = "nome_mae", length = 200)
    public String nomeMae;

    @Column(name = "nome_pai", length = 200)
    public String nomePai;

    @Column(length = 30)
    public String raca;

    @Column(length = 50)
    public String religiao;

    @Column(length = 60)
    public String nacionalidade;

    @Column(length = 100)
    public String naturalidade;

    @Column(name = "tipo_sanguineo", length = 5)
    public String tipoSanguineo;

    @Column(name = "estado_civil", length = 30)
    public String estadoCivil;

    @Column(name = "possui_dependentes")
    public boolean possuiDependentes;

    public boolean pcd;

    @Column(name = "tipo_deficiencia", length = 60)
    public String tipoDeficiencia;

    @Column(length = 60)
    public String escolaridade;

    @Column(name = "instituicao_escolar", length = 200)
    public String instituicaoEscolar;

    @Column(name = "data_conclusao_escolar")
    public LocalDate dataConclusaoEscolar;

    @Column(length = 200)
    public String email;

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

    public void aplicar(String nomeCompleto, String nomeSocial, String sexo,
            LocalDate dataNascimento, String nomeMae, String nomePai, String raca, String religiao,
            String nacionalidade, String naturalidade, String tipoSanguineo, String estadoCivil,
            boolean possuiDependentes, boolean pcd, String tipoDeficiencia, String escolaridade,
            String instituicaoEscolar, LocalDate dataConclusaoEscolar, String email,
            String logradouro, String numero, String complemento, String bairro, String cidade,
            String uf, String cep) {
        this.nomeCompleto = nomeCompleto;
        this.nomeSocial = nomeSocial;
        this.sexo = sexo;
        this.dataNascimento = dataNascimento;
        this.nomeMae = nomeMae;
        this.nomePai = nomePai;
        this.raca = raca;
        this.religiao = religiao;
        this.nacionalidade = nacionalidade;
        this.naturalidade = naturalidade;
        this.tipoSanguineo = tipoSanguineo;
        this.estadoCivil = estadoCivil;
        this.possuiDependentes = possuiDependentes;
        this.pcd = pcd;
        this.tipoDeficiencia = tipoDeficiencia;
        this.escolaridade = escolaridade;
        this.instituicaoEscolar = instituicaoEscolar;
        this.dataConclusaoEscolar = dataConclusaoEscolar;
        this.email = email;
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