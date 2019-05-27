package facetec.client.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import facetec.client.controller.ClientLoginController;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class FacetecClientService {

    @Value("${facetec.server.url:https://www.facetec.tk/}")
    private String facetecServerUrl;

    @Autowired
    private ClientLoginController controller;

    public CloseableHttpResponse login(String usuario, String senha) {
        CloseableHttpClient client = HttpClients.createDefault();

        try {
            HttpPost httpPost = new HttpPost(facetecServerUrl + "welcome");
            StringEntity entity = new StringEntity("{ \"username\": \"" + usuario + "\", \"password\": \"" + senha + "\" }");
            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            return client.execute(httpPost);
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

    public List<Map<String, Object>> getPessoasPendentes() {
        if (StringUtils.isEmpty(this.controller.getCurrentUser())) {
            return new ArrayList<>();
        }
        try {
            String pendenteIntegracao = get("integracaoPessoa/pendentes/" + controller.getCurrentUser());
            return new ObjectMapper().readValue(pendenteIntegracao, new TypeReference<List<Map<String, Object>>>() {});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, Object> getDevices() {
        try {
            String pendenteIntegracao = get("integracaoPessoa/devices/" + controller.getCurrentUser());
            return new ObjectMapper().readValue(pendenteIntegracao, new TypeReference<Map<String, Object>>() {});
        } catch (IOException e) {
            throw new RuntimeException(e);
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

    private String get(String path) {
        CloseableHttpClient client = HttpClients.createDefault();
        try {
            HttpGet httpGet = new HttpGet(facetecServerUrl + path + "/");
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

}
