package facetec.core.service;

/**
 * Created by rkogawa on 06/07/19.
 */
public class IntegracaoPessoaDeviceVO {

    private String url;

    private String contentType;

    private String password;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
