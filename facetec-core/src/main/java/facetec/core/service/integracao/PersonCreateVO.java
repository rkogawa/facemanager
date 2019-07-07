package facetec.core.service.integracao;

import facetec.core.service.IntegracaoPessoaService;

/**
 * Created by rkogawa on 21/05/19.
 */
public class PersonCreateVO {

    private final String pass = IntegracaoPessoaService.PARAM_FIELD_PASSWORD;

    private final PersonVO person;

    public PersonCreateVO(PersonVO person) {
        this.person = person;
    }

    public String getPass() {
        return pass;
    }

    public PersonVO getPerson() {
        return person;
    }

}
