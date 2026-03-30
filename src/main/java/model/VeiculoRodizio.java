package model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "veiculo_rodizio")
public class VeiculoRodizio {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID")
    public UUID id;

    @Column(name = "final_placa", nullable = false, unique = true)
    public Short finalPlaca;

    @Column(length = 20, nullable = false)
    public String descricao;

    @Column(nullable = false)
    public boolean status = true;

    public void aplicar(Short finalPlaca, String descricao) {
        this.finalPlaca = finalPlaca;
        this.descricao = descricao;
    }
}