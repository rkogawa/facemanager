package facetec.core.domain;

import facetec.core.security.domain.LocalidadeUsuario;

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
 * Created by rkogawa on 08/05/19.
 */
@Entity
@Table(name = "GRUPO")
@SequenceGenerator(name = "SEQ_GRUPO", sequenceName = "SEQ_GRUPO")
public class Grupo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_GRUPO")
    @Column(name = "COD_GRUPO", nullable = false)
    private Long id;

    @Column(name = "NOME", nullable = false)
    private String nome;

    @ManyToOne
    @JoinColumn(name = "COD_LOCALIDADE_USUARIO", nullable = false, foreignKey = @ForeignKey(name = "FK_GRUPO_LOCALIDADE"))
    private LocalidadeUsuario localidade;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalidadeUsuario getLocalidade() {
        return localidade;
    }

    public void setLocalidade(LocalidadeUsuario localidade) {
        this.localidade = localidade;
    }
}
