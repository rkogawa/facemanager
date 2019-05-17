package facetec.client.controller;

import facetec.client.service.ClientDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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

    @RequestMapping(value = "person/create/{ip:.+}", method = RequestMethod.POST)
    public String personCreate(@PathVariable String ip, @RequestBody String params) {
        return service.postWithParams(ip,"person/create", params);
    }

    @RequestMapping(value = "person/update/{ip:.+}", method = RequestMethod.POST)
    public String personUpdate(@PathVariable String ip, @RequestBody String params) {
        return service.postWithParams(ip,"person/update", params);
    }

    @RequestMapping(value = "person/delete/{ip:.+}", method = RequestMethod.POST)
    public String personDelete(@PathVariable String ip, @RequestBody String params) {
        return service.postWithParams(ip,"person/update", params);
    }

    @RequestMapping(value = "face/create/{ip:.+}", method = RequestMethod.POST)
    public String faceCreate(@PathVariable String ip, @RequestBody String params) {
        return service.postWithParams(ip,"face/create", params);
    }

    @RequestMapping(value = "person/permissionsDelete/{ip:.+}", method = RequestMethod.POST)
    public String permissionsDelete(@PathVariable String ip, @RequestBody String params) {
        return service.postWithParams(ip,"person/permissionsDelete", params);
    }

    @RequestMapping(value = "person/permissionsCreate/{ip:.+}", method = RequestMethod.POST)
    public String permissionsCreate(@PathVariable String ip, @RequestBody String params) {
        return service.postWithParams(ip,"person/permissionsCreate", params);
    }
}
