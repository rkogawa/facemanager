package facetec.core.domain;

import facetec.core.domain.enumx.StatusIntegracaoPessoa;

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
 * Created by rkogawa on 21/05/19.
 */
@Entity
@Table(name = "INTEGRACAO_PESSOA")
@SequenceGenerator(name = "SEQ_INTEGRACAO_PESSOA", sequenceName = "SEQ_INTEGRACAO_PESSOA")
public class IntegracaoPessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_INTEGRACAO_PESSOA")
    @Column(name = "COD_INTEGRACAO_PESSOA", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "COD_PESSOA", foreignKey = @ForeignKey(name = "FK_INTPES_PESSOA"))
    private Pessoa pessoa;

    @Column(name = "STATUS", nullable = false)
    private StatusIntegracaoPessoa status;

    @Column(name = "LOG_INTEGRACAO")
    private String logIntegracao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public StatusIntegracaoPessoa getStatus() {
        return status;
    }

    public void setStatus(StatusIntegracaoPessoa status) {
        this.status = status;
    }

    public String getLogIntegracao() {
        return logIntegracao;
    }

    public void setLogIntegracao(String logIntegracao) {
        this.logIntegracao = logIntegracao;
    }
}
