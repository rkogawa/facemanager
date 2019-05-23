package facetec.client.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import facetec.client.controller.ClientController;
import org.apache.http.Header;
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
import java.util.List;
import java.util.Map;

@Service
public class FacetecClientService {

    @Value("${facetec.server.url:https://www.facetec.tk/}")
    private String facetecServerUrl;

    @Autowired
    private ClientController controller;

    public String login(String usuario, String senha) {
        CloseableHttpClient client = HttpClients.createDefault();

        try {
            HttpPost httpPost = new HttpPost(facetecServerUrl + "login");
            StringEntity entity = new StringEntity("{ \"username\": \"" + usuario + "\", \"password\": \"" + senha + "\" }");
            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            CloseableHttpResponse response = client.execute(httpPost);
            Header[] headers = response.getHeaders("Failure");
            return headers.length > 0 ? headers[0].getValue() : null;
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
        try {
            String pendenteIntegracao = findPessoasPendenteIntegracao();
            return new ObjectMapper().readValue(pendenteIntegracao, new TypeReference<List<Map<String, Object>>>() {});
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

    private String findPessoasPendenteIntegracao() {
        CloseableHttpClient client = HttpClients.createDefault();
        try {
            HttpGet httpGet = new HttpGet(facetecServerUrl + "integracaoPessoa/pendentes/" + controller.getCurrentUser());
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
