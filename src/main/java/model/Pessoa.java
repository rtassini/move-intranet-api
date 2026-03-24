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