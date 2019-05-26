package facetec.client.controller;

import facetec.client.ClientApplication;
import facetec.client.service.FacetecClientService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ClientController {

    private String currentUser;

    @FXML private Text actiontarget;

    @FXML
    private TextField usuarioField;

    @FXML
    private PasswordField passwordField;

    @Autowired
    private FacetecClientService service;

    @Value("${facetec.client.url:https://www.facetec.tk/}")
    private String url;

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
                Parent page = FXMLLoader.load(ClientApplication.class.getResource("/fxml/home.fxml"), null, new JavaFXBuilderFactory());
                Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                currentStage.setScene(new Scene(page, 823, 600));
                currentStage.show();

                ClientApplication.getInstance().getHostServices().showDocument(url + "?token="+token);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public String getCurrentUser() {
        return currentUser;
    }
}
