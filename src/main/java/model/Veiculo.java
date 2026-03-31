package model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "veiculos")
public class Veiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID")
    public UUID id;

    @Column(length = 10, nullable = false, unique = true)
    public String placa;

    @Column(length = 20, nullable = false, unique = true)
    public String renavam;

    @Column(length = 50, nullable = false, unique = true)
    public String chassi;

    @Column(length = 100)
    public String descricao;

    @Column(name = "ano_fabricacao")
    public Short anoFabricacao;

    @Column(name = "ano_modelo")
    public Short anoModelo;

    @Column(length = 30)
    public String cor;

    @Column(length = 30)
    public String combustivel;

    @Column(length = 100)
    public String proprietario;

    @Column(name = "cpf_cnpj", length = 18)
    public String cpfCnpj;

    @Column(length = 20, nullable = false)
    public String situacao = "DISPONIVEL";

    @Column(nullable = false)
    public boolean status = true;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "empresa_id", nullable = false)
    public Empresa empresa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rodizio_id")
    public VeiculoRodizio rodizio;

    @Column(name = "created_at", updatable = false)
    public LocalDateTime createdAt;

    @Column(name = "updated_at")
    public LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    public LocalDateTime deletedAt;

    public void aplicar(String placa, String renavam, String chassi, String descricao,
            Short anoFabricacao, Short anoModelo, String cor, String combustivel,
            String proprietario, String cpfCnpj, String situacao) {
        this.placa = placa;
        this.renavam = renavam;
        this.chassi = chassi;
        this.descricao = descricao;
        this.anoFabricacao = anoFabricacao;
        this.anoModelo = anoModelo;
        this.cor = cor;
        this.combustivel = combustivel;
        this.proprietario = proprietario;
        this.cpfCnpj = cpfCnpj;
        this.situacao = situacao != null && !situacao.isBlank() ? situacao : "DISPONIVEL";
    }

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}