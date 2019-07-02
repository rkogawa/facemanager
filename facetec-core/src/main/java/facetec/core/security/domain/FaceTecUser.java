package facetec.core.security.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Created by rkogawa on 06/05/19.
 */
@Entity
@Table(name = "FACETEC_USER")
@SequenceGenerator(name = "SEQ_FACETEC_USER", sequenceName = "SEQ_FACETEC_USER")
public class FaceTecUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_FACETEC_USER")
    @Column(name = "COD_USER", nullable = false)
    private Long id;

    @Column(name = "USERNAME", nullable = false)
    private String username;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "ADMIN", nullable = false)
    private boolean admin;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "COD_LOCALIDADE_USUARIO", nullable = false, foreignKey = @ForeignKey(name = "FK_USER_LOCALIDADE"))
    private LocalidadeUsuario localidade;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public LocalidadeUsuario getLocalidade() {
        return localidade;
    }

    public void setLocalidade(LocalidadeUsuario localidade) {
        this.localidade = localidade;
    }
}
