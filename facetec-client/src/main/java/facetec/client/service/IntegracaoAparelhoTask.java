package facetec.client.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by rkogawa on 20/05/19.
 */
@Service
public class IntegracaoAparelhoTask {

    private static final Log LOG = LogFactory.getLog(IntegracaoAparelhoTask.class);

    @Autowired
    private ClientDeviceService service;

    @Autowired
    private FacetecClientService clientService;

    @Scheduled(cron = "*/10 * * * * *")
    public void executeTask() {
        StringBuilder logIntegracao = new StringBuilder();
        clientService.getPessoasPendentes().forEach(i -> {
            final ResponseTask response = new ResponseTask();
            response.setId((String) i.get("id"));
            response.setSuccess(true);
            ((List<Map>) i.get("devices")).forEach(d -> {

                for (Map request : (List<Map>) i.get("requests")) {
                    String requestPath = (String) request.get("requestPath");
                    String paramsJSON = (String) request.get("paramsJSON");
                    try {
                        String responseMsg = service.post((String) d.get("url"), (String) d.get("contentType"), requestPath, paramsJSON);
                        if (paramsJSON.contains("\"shouldFail\":true") && responseMsg.contains("\"success\":false") && responseMsg.contains("\"msg\":")) {
                            String errorMessage = (String) new ObjectMapper().readValue(responseMsg, Map.class).get("msg");
                            throw new RuntimeException(String.format("Resultado inesperado: %s", errorMessage));
                        }
                        logIntegracao.append(String.format("Requisição %s executada com sucesso em %s.\n", requestPath, d));
                    } catch (Exception e) {
                        logIntegracao.append(String.format("Erro na requisição %s em %s - %s.\n", requestPath, d, e.getMessage()));
                        LOG.error(String.format("Erro na requisição %s em %s com parâmetros %s - %s.", requestPath, d, paramsJSON, e.getMessage()), e);
                        response.setSuccess(false);
                        break;

                    }
                }
            });

            clientService.updateIntegracao(response.getId(), response.isSuccess(), logIntegracao.toString());
        });
    }

    class ResponseTask {

        private String id;

        private boolean success;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }
    }
}
