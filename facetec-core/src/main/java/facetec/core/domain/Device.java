package facetec.core.domain;

import facetec.core.domain.enumx.ClassificacaoDevice;
import facetec.core.security.domain.FaceTecUser;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Created by rkogawa on 09/05/19.
 */
@Entity
@Table(name = "DEVICE")
@SequenceGenerator(name = "SEQ_DEVICE", sequenceName = "SEQ_DEVICE")
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_DEVICE")
    @Column(name = "COD_DEVICE", nullable = false)
    private Long id;

    @Column(name = "IP", nullable = false)
    private String ip;

    @Column(name = "NOME", nullable = false)
    private String nome;

    @Column(name = "CLASSIFICACAO", nullable = false)
    private ClassificacaoDevice classificacao;

    @ManyToOne
    @JoinColumn(name = "COD_USER", nullable = false, foreignKey = @ForeignKey(name = "FK_DEVICE_USER"))
    private FaceTecUser predio;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public ClassificacaoDevice getClassificacao() {
        return classificacao;
    }

    public void setClassificacao(ClassificacaoDevice classificacao) {
        this.classificacao = classificacao;
    }

    public FaceTecUser getPredio() {
        return predio;
    }

    public void setPredio(FaceTecUser predio) {
        this.predio = predio;
    }
}
