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
import org.springframework.beans.factory.annotation.Autowired;
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

    @FXML protected void handleSubmitButtonAction(ActionEvent event) {
        String login = service.login(usuarioField.getText(), passwordField.getText());
        if (login != null) {
                actiontarget.setText(login);
        } else {
            currentUser = usuarioField.getText();
            try {
                Parent page = FXMLLoader.load(ClientApplication.class.getResource("/fxml/home.fxml"), null, new JavaFXBuilderFactory());
                Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                currentStage.setScene(new Scene(page, 823, 600));
                currentStage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public String getCurrentUser() {
        return currentUser;
    }
}
