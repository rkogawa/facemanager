package facetec.core.service.integracao;

/**
 * Created by rkogawa on 21/05/19.
 */
public class PersonDeleteVO {

    private final String pass;

    private final String id;

    public PersonDeleteVO(String pass, String id) {
        this.pass = pass;
        this.id = id;
    }

    public String getPass() {
        return pass;
    }

    public String getId() {
        return id;
    }
}
