package facetec.core.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
