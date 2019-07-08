package facetec.core.service;

/**
 * Created by rkogawa on 09/05/19.
 */
public class PessoaResponseVO {

    private Long id;

    private Long dataHoraFim;

    private Long integracaoId;

    private String foto;

    private boolean integracaoAgendada;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDataHoraFim() {
        return dataHoraFim;
    }

    public void setDataHoraFim(Long dataHoraFim) {
        this.dataHoraFim = dataHoraFim;
    }

    public Long getIntegracaoId() {
        return integracaoId;
    }

    public void setIntegracaoId(Long integracaoId) {
        this.integracaoId = integracaoId;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public boolean isIntegracaoAgendada() {
        return integracaoAgendada;
    }

    public void setIntegracaoAgendada(boolean integracaoAgendada) {
        this.integracaoAgendada = integracaoAgendada;
    }
}
