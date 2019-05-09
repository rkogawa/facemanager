package facetec.core.domain;

import facetec.core.domain.enumx.InformacaoAcessoPessoa;
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
import java.time.LocalDateTime;

/**
 * Created by rkogawa on 08/05/19.
 */
@Entity
@Table(name = "PESSOA")
@SequenceGenerator(name = "SEQ_PESSOA", sequenceName = "SEQ_PESSOA")
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_PESSOA")
    @Column(name = "COD_PESSOA", nullable = false)
    private Long id;

    @Column(name = "NOME", nullable = false)
    private String nome;

    @Column(name = "CPF", nullable = false)
    private String cpf;

    @ManyToOne
    @JoinColumn(name = "COD_GRUPO", foreignKey = @ForeignKey(name = "FK_PESSOA_GRUPO"))
    private Grupo grupo;

    @Column(name = "TELEFONE")
    private String telefone;

    @Column(name = "CELULAR")
    private String celular;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "INFORMACAO_ACESSO", nullable = false)
    private InformacaoAcessoPessoa informacaoAcesso;

    @Column(name = "DT_HORA_INICIO")
    private LocalDateTime dataHoraInicio;

    @Column(name = "DT_HORA_FIM")
    private LocalDateTime dataHoraFim;

    @Column(name = "DT_HORA_ULT_ACESSO")
    private LocalDateTime ultimoAcesso;

    @Column(name = "COMENTARIO")
    private String comentario;

    @ManyToOne
    @JoinColumn(name = "COD_USER", nullable = false, foreignKey = @ForeignKey(name = "FK_PESSOA_USER"))
    private FaceTecUser predio;

    @Column(name = "FOTO")
    private String foto;

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

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public InformacaoAcessoPessoa getInformacaoAcesso() {
        return informacaoAcesso;
    }

    public void setInformacaoAcesso(InformacaoAcessoPessoa informacaoAcesso) {
        this.informacaoAcesso = informacaoAcesso;
    }

    public LocalDateTime getDataHoraInicio() {
        return dataHoraInicio;
    }

    public void setDataHoraInicio(LocalDateTime dataHoraInicio) {
        this.dataHoraInicio = dataHoraInicio;
    }

    public LocalDateTime getDataHoraFim() {
        return dataHoraFim;
    }

    public void setDataHoraFim(LocalDateTime dataHoraFim) {
        this.dataHoraFim = dataHoraFim;
    }

    public LocalDateTime getUltimoAcesso() {
        return ultimoAcesso;
    }

    public void setUltimoAcesso(LocalDateTime ultimoAcesso) {
        this.ultimoAcesso = ultimoAcesso;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public FaceTecUser getPredio() {
        return predio;
    }

    public void setPredio(FaceTecUser predio) {
        this.predio = predio;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
