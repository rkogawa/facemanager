package facetec.core.service.integracao;

/**
 * Created by rkogawa on 21/05/19.
 */
public class PermissionDeleteVO {

    private final String pass;

    private final String personId;

    public PermissionDeleteVO(String pass, String personId) {
        this.pass = pass;
        this.personId = personId;
    }

    public String getPass() {
        return pass;
    }

    public String getPersonId() {
        return personId;
    }
}
