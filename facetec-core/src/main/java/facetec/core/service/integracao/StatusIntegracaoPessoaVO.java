package facetec.core.service.integracao;

/**
 * Created by rkogawa on 21/05/19.
 */
public class StatusIntegracaoPessoaVO {

    private final String status;

    public StatusIntegracaoPessoaVO(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
