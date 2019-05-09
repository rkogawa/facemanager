package facetec.core.controller;

import facetec.core.service.DeviceService;
import facetec.core.service.DeviceVO;
import facetec.core.service.DevicesVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by rkogawa on 09/05/19.
 */
@RestController
@RequestMapping("/device")
@Transactional
public class DeviceRestController {

    @Autowired
    private DeviceService service;

    @RequestMapping(method = RequestMethod.POST)
    public void create(@RequestBody DevicesVO devices) {
        service.create(devices);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<DeviceVO> findDevices() {
        return service.findDevices();
    }
}
