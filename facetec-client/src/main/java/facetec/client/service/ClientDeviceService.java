package facetec.client.service;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by rkogawa on 15/05/19.
 */
@Service
public class ClientDeviceService {

    public String getDeviceKey(String ip) {
//        try {
//            Socket socket = new Socket(ip, 8088);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

        CloseableHttpClient client = HttpClients.createDefault();
        try {
            HttpPost httpPost = new HttpPost("http://" + ip + ":8088/getDeviceKey");
            //            String json = "{\"pass\": \"12345\", \"id\":\"-1\"}";
            //            StringEntity entity = new StringEntity(json);
            //            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            CloseableHttpResponse response = client.execute(httpPost);
            String responseBody = EntityUtils.toString(response.getEntity());
            client.close();
            return responseBody;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
