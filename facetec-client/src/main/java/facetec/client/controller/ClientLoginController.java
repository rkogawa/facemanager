package facetec.client.controller;

import facetec.client.ClientApplication;
import facetec.client.service.FacetecClientService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ClientLoginController {

    private String currentUser;

    @FXML private Text actiontarget;

    @FXML
    private TextField usuarioField;

    @FXML
    private PasswordField passwordField;

    @Autowired
    private FacetecClientService service;

    @Value("${facetec.client.url:https://www.facetec.tk/ftca888/}")
    private String url;

    @Autowired
    private ClientHomeController clientHomeController;

    @FXML protected void handleSubmitButtonAction(ActionEvent event) {
        CloseableHttpResponse response = service.login(usuarioField.getText(), passwordField.getText());
        Header[] headers = response.getHeaders("Failure");
        String failure = headers.length > 0 ? headers[0].getValue() : null;
        if (failure != null) {
                actiontarget.setText(failure);
        } else {
            currentUser = usuarioField.getText();
            String token = response.getHeaders("Authorization")[0].getValue();
            try {
                clientHomeController.loadHome(event);

                ClientApplication.getInstance().getHostServices().showDocument(url + "?token="+token);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public String getUrl() {
        return url;
    }

    public String getCurrentUser() {
        return currentUser;
    }
}
