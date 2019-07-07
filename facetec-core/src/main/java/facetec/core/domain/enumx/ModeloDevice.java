package facetec.core.domain.enumx;

/**
 * Created by rkogawa on 06/07/19.
 */
public enum ModeloDevice {

    KF("application/json"), HS("application/x-www-form-urlencoded");

    private final String contentType;

    ModeloDevice(String contentType) {
        this.contentType = contentType;
    }

    public String getContentType() {
        return contentType;
    }
}
