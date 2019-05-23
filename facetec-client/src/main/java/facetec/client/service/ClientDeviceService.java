package facetec.client.service;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by rkogawa on 15/05/19.
 */
@Service
public class ClientDeviceService {

    @Value("${facetec.client.deviceBaseUrl:http://%s:8088/%s}")
    private String deviceBaseUrl;

    public String post(String url, String requestPath, String jsonParams) {
        CloseableHttpClient client = HttpClients.createDefault();

        try {
            HttpPost httpPost = new HttpPost(getDeviceUrl(url, requestPath));
            if (jsonParams != null) {
                StringEntity entity = new StringEntity(jsonParams);
                httpPost.setEntity(entity);
            }
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            CloseableHttpResponse response = client.execute(httpPost);
            return EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private String getDeviceUrl(String url, String requestPath) {
        return String.format("%s%s", url, requestPath);
    }

}
