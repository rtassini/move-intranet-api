package model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "locais")
public class Local {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID")
    public UUID id;

    @Column(length = 60, nullable = false)
    public String descricao;

    @Column(length = 2)
    public String uf;

    @Column(nullable = false)
    public boolean status = true;

    public void aplicar(String descricao, String uf) {
        this.descricao = descricao;
        this.uf = uf;
    }
}