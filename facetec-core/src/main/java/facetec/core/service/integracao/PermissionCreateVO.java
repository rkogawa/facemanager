package facetec.core.service.integracao;

/**
 * Created by rkogawa on 21/05/19.
 */
public class PermissionCreateVO {

    private final String pass;

    private final String personId;

    private final Long time;

    public PermissionCreateVO(String pass, String personId, Long time) {
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

    public Long getTime() {
        return time;
    }
}
