package facetec.core.service.integracao;

/**
 * Created by rkogawa on 21/05/19.
 */
public class PersonCreateVO {

    private final String pass;

    private final PersonVO person;

    public PersonCreateVO(String pass, PersonVO person) {
        this.pass = pass;
        this.person = person;
    }

    public String getPass() {
        return pass;
    }

    public PersonVO getPerson() {
        return person;
    }

}
