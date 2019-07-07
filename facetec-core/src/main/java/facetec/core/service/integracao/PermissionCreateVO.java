package facetec.core.service.integracao;

import facetec.core.service.IntegracaoPessoaService;

/**
 * Created by rkogawa on 21/05/19.
 */
public class PermissionCreateVO {

    private final String pass = IntegracaoPessoaService.PARAM_FIELD_PASSWORD;

    private final String personId;

    private final Long time;

    public PermissionCreateVO(String personId, Long time) {
        this.personId = personId;
        this.time = time;
    }

    public String getPass() {
        return pass;
    }

    public String getPersonId() {
        return personId;
    }

    public Long getTime() {
        return time;
    }
}
