package facetec.core.security.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Created by rkogawa on 30/06/19.
 */
@Entity
@Table(name = "LOCALIDADE_USUARIO")
@SequenceGenerator(name = "SEQ_LOCALIDADE_USUARIO", sequenceName = "SEQ_LOCALIDADE_USUARIO")
public class LocalidadeUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_LOCALIDADE_USUARIO")
    @Column(name = "COD_LOCALIDADE_USUARIO", nullable = false)
    private Long id;

    @Column(name = "NOME", nullable = false)
    private String nome;

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
}
