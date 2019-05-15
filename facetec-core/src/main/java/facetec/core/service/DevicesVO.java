package facetec.core.service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rkogawa on 09/05/19.
 */
public class DevicesVO {

    private List<DeviceVO> devices = new ArrayList<>();

    private String adminPassword;

    public List<DeviceVO> getDevices() {
        return devices;
    }

    public void setDevices(List<DeviceVO> devices) {
        this.devices = devices;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }
}
