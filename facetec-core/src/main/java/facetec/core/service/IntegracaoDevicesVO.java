package facetec.core.service;

import java.util.List;

/**
 * Created by rkogawa on 26/05/19.
 */
public class IntegracaoDevicesVO {

    private List<IntegracaoDeviceVO> devices;

    private String requestPath;

    public List<IntegracaoDeviceVO> getDevices() {
        return devices;
    }

    public void setDevices(List<IntegracaoDeviceVO> devices) {
        this.devices = devices;
    }

    public String getRequestPath() {
        return requestPath;
    }

    public void setRequestPath(String requestPath) {
        this.requestPath = requestPath;
    }
}
