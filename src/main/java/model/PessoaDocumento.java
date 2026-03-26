package model;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "pessoa_documentos")
public class PessoaDocumento {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID")
    public UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pessoa_id", nullable = false)
    public Pessoa pessoa;

    @Column(length = 14, nullable = false)
    public String cpf;

    @Column(length = 20)
    public String rg;

    @Column(name = "data_expedicao_rg")
    public LocalDate dataExpedicaoRg;

    @Column(name = "orgao_emissor_rg", length = 10)
    public String orgaoEmissorRg;

    @Column(name = "pis_pasep", length = 20)
    public String pisPasep;

    @Column(length = 20)
    public String nis;

    @Column(length = 20)
    public String cns;

    @Column(name = "titulo_eleitor", length = 20)
    public String tituloEleitor;

    @Column(name = "titulo_eleitor_zona", length = 10)
    public String tituloEleitorZona;

    @Column(name = "titulo_eleitor_secao", length = 10)
    public String tituloEleitorSecao;

    @Column(length = 20)
    public String reservista;

    @Column(name = "numero_ctps", length = 20)
    public String numeroCtps;

    @Column(name = "serie_ctps", length = 10)
    public String serieCtps;

    @Column(name = "data_expedicao_ctps")
    public LocalDate dataExpedicaoCtps;

    @Column(name = "uf_ctps", length = 2)
    public String ufCtps;

    @Column(name = "numero_cnh", length = 20)
    public String numeroCnh;

    @Column(name = "categoria_cnh", length = 5)
    public String categoriaCnh;

    @Column(name = "validade_cnh")
    public LocalDate validadeCnh;

    public void aplicar(String cpf, String rg, LocalDate dataExpedicaoRg, String orgaoEmissorRg,
            String pisPasep, String nis, String cns, String tituloEleitor,
            String tituloEleitorZona, String tituloEleitorSecao, String reservista,
            String numeroCtps, String serieCtps, LocalDate dataExpedicaoCtps, String ufCtps,
            String numeroCnh, String categoriaCnh, LocalDate validadeCnh) {
        this.cpf = cpf;
        this.rg = rg;
        this.dataExpedicaoRg = dataExpedicaoRg;
        this.orgaoEmissorRg = orgaoEmissorRg;
        this.pisPasep = pisPasep;
        this.nis = nis;
        this.cns = cns;
        this.tituloEleitor = tituloEleitor;
        this.tituloEleitorZona = tituloEleitorZona;
        this.tituloEleitorSecao = tituloEleitorSecao;
        this.reservista = reservista;
        this.numeroCtps = numeroCtps;
        this.serieCtps = serieCtps;
        this.dataExpedicaoCtps = dataExpedicaoCtps;
        this.ufCtps = ufCtps;
        this.numeroCnh = numeroCnh;
        this.categoriaCnh = categoriaCnh;
        this.validadeCnh = validadeCnh;
    }
}