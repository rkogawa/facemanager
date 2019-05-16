package facetec.client.controller;

import facetec.client.service.ClientDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by rkogawa on 15/05/19.
 */
@RestController
@RequestMapping
public class ClientDeviceRestController {

    @Autowired
    private ClientDeviceService service;

    @RequestMapping(value = "getDeviceKey/{ip:.+}", method = RequestMethod.POST)
    public String getDeviceKey(@PathVariable String ip) {
        return service.getDeviceKey(ip);
    }

}
