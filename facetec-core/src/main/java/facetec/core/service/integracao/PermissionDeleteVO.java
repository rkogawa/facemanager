package facetec.core.service.integracao;

import facetec.core.service.IntegracaoPessoaService;

/**
 * Created by rkogawa on 21/05/19.
 */
public class PermissionDeleteVO {

    private final String pass = IntegracaoPessoaService.PARAM_FIELD_PASSWORD;

    private final String personId;

    public PermissionDeleteVO(String personId) {
        this.personId = personId;
    }

    public String getPass() {
        return pass;
    }

    public String getPersonId() {
        return personId;
    }
}
