package facetec.core.service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rkogawa on 09/05/19.
 */
public class DevicesVO {

    private List<DeviceVO> devices = new ArrayList<>();

    public List<DeviceVO> getDevices() {
        return devices;
    }

    public void setDevices(List<DeviceVO> devices) {
        this.devices = devices;
    }
}
