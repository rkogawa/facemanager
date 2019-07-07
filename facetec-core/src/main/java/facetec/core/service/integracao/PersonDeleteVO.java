package facetec.core.service.integracao;

import facetec.core.service.IntegracaoPessoaService;

/**
 * Created by rkogawa on 21/05/19.
 */
public class PersonDeleteVO {

    private final String pass = IntegracaoPessoaService.PARAM_FIELD_PASSWORD;

    private final String id;

    public PersonDeleteVO(String id) {
        this.id = id;
    }

    public String getPass() {
        return pass;
    }

    public String getId() {
        return id;
    }
}
