package facetec.core.service;

/**
 * Created by rkogawa on 21/05/19.
 */
public class IntegracaoPessoaRequestVO {

    private final String requestPath;

    private String paramsJSON;

    public IntegracaoPessoaRequestVO(String requestPath) {
        this.requestPath = requestPath;
    }

    public String getRequestPath() {
        return requestPath;
    }

    public String getParamsJSON() {
        return paramsJSON;
    }

    public void setParamsJSON(String paramsJSON) {
        this.paramsJSON = paramsJSON;
    }
}
