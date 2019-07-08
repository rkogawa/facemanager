package facetec.core.service.integracao;

/**
 * Created by rkogawa on 21/05/19.
 */
public class PermissionCreateVO {

    private final String pass;

    private final String personId;

    private final String time;

    public PermissionCreateVO(String pass, String personId, String time) {
        this.pass = pass;
        this.personId = personId;
        this.time = time;
    }

    public String getPass() {
        return pass;
    }

    public String getPersonId() {
        return personId;
    }

    public String getTime() {
        return time;
    }
}
