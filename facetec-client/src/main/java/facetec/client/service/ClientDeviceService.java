package facetec.client.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by rkogawa on 15/05/19.
 */
@Service
public class ClientDeviceService {

    public String post(String url, String contentType, String requestPath, String jsonParams) {
        CloseableHttpClient client = HttpClients.createDefault();

        try {
            HttpPost httpPost = new HttpPost(getDeviceUrl(url, requestPath));
            if (jsonParams != null) {
                fillPostParams(contentType, jsonParams, httpPost);
            }

            httpPost.setHeader("Accept", contentType);
            httpPost.setHeader("Content-type", contentType);

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

    private void fillPostParams(String contentType, String jsonParams, HttpPost httpPost) throws IOException {
        if ("application/x-www-form-urlencoded".equals(contentType)) {
            List<NameValuePair> nvps = new ArrayList<>();
            Map<String, Object> params = new ObjectMapper().readValue(jsonParams, Map.class);
            for (String key : params.keySet()) {
                String paramValue;
                if (params.get(key) instanceof Map) {
                    paramValue = new ObjectMapper().writeValueAsString(params.get(key));
                } else {
                    paramValue = params.get(key).toString();
                }
                nvps.add(new BasicNameValuePair(key, paramValue));
            }

            httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
        } else {
            StringEntity entity = new StringEntity(jsonParams);
            httpPost.setEntity(entity);
        }
    }

    private String getDeviceUrl(String url, String requestPath) {
        return String.format("%s%s", url, requestPath);
    }

}
