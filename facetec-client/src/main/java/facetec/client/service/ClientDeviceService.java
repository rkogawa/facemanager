package facetec.client.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by rkogawa on 15/05/19.
 */
@Service
public class ClientDeviceService {

    @Value("${facetec.client.deviceBaseUrl:http://%s:8088/%s}")
    private String deviceBaseUrl;

    @Value("${facetec.server.url:https://www.facetec.tk/}")
    private String facetecServerUrl;

    public List<Map<String, Object>> getPessoasPendentes() {
        try {
            String pendenteIntegracao = findPessoasPendenteIntegracao();
            return new ObjectMapper().readValue(pendenteIntegracao, new TypeReference<List<Map<String, Object>>>() {});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String findPessoasPendenteIntegracao() {
        CloseableHttpClient client = HttpClients.createDefault();
        try {
            HttpGet httpGet = new HttpGet(facetecServerUrl + "integracaoPessoa/pendentes/facetec");
            CloseableHttpResponse response = client.execute(httpGet);
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

    public String updateIntegracao(String id, Boolean success, String logIntegracao) {
        CloseableHttpClient client = HttpClients.createDefault();
        try {
            HttpPut put = new HttpPut(facetecServerUrl + "integracaoPessoa/" + id);
            StringEntity entity = new StringEntity("{ \"success\":" + success + " }");
            put.setEntity(entity);
            put.setHeader("Accept", "application/json");
            put.setHeader("Content-type", "application/json");
            CloseableHttpResponse response = client.execute(put);
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
