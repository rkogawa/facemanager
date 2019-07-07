package facetec.core.service.integracao;

import facetec.core.service.IntegracaoPessoaService;

/**
 * Created by rkogawa on 21/05/19.
 */
public class FaceCreateVO {

    private final String pass = IntegracaoPessoaService.PARAM_FIELD_PASSWORD;

    private final String personId;

    private final String imgBase64;

    public FaceCreateVO(String personId, String imgBase64) {
        this.personId = personId;
        this.imgBase64 = imgBase64;
    }

    public String getPass() {
        return pass;
    }

    public String getPersonId() {
        return personId;
    }

    public String getImgBase64() {
        return imgBase64;
    }
}
