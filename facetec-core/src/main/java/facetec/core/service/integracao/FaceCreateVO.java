package facetec.core.service.integracao;

/**
 * Created by rkogawa on 21/05/19.
 */
public class FaceCreateVO {

    private final String pass;

    private final String personId;

    private final String faceId = "";

    private final String imgBase64;

    private final boolean shouldFail = true;

    public FaceCreateVO(String pass, String personId, String imgBase64) {
        this.pass = pass;
        this.personId = personId;
        this.imgBase64 = imgBase64;
    }

    public String getPass() {
        return pass;
    }

    public String getPersonId() {
        return personId;
    }

    public String getFaceId() {
        return faceId;
    }

    public String getImgBase64() {
        return imgBase64;
    }

    public boolean isShouldFail() {
        return shouldFail;
    }
}
