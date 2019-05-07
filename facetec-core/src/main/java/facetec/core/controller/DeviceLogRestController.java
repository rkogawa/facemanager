package facetec.core.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

/**
 * Created by rkogawa on 04/05/19.
 */
@RestController
@RequestMapping
public class DeviceLogRestController {

    @RequestMapping(value = "/upRecord", method = RequestMethod.POST)
    public void upRecord(@RequestBody String params) throws IOException {
//        Timestamp time = new Timestamp(vo.getTime());
        Map<String, Object> paramsMap = new ObjectMapper().readValue(params, Map.class);
        System.out.println(paramsMap.get("Name"));
        System.out.println(paramsMap.get("Score"));
        System.out.println(paramsMap.get("Result"));
        System.out.println(new Timestamp((Long) paramsMap.get("Time")));
    }

    @RequestMapping(value = "/teste", method = RequestMethod.POST, produces = APPLICATION_JSON_UTF8_VALUE)
    public void teste() {
        System.out.println("Teste");
    }
}
