package facetec.client.controller;

import facetec.client.ClientApplication;
import facetec.client.service.ClientDeviceService;
import facetec.client.service.FacetecClientService;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by rkogawa on 26/05/19.
 */
@Component
public class ClientHomeController {

    @Autowired
    private FacetecClientService service;

    @Autowired
    private ClientDeviceService deviceService;

    @FXML
    private TableView<DeviceStatusVO> tableView;

    @FXML
    protected void handleSubmitButtonAction(ActionEvent event) {
        Map<String, Object> devices = service.getDevices();
        String requestPath = (String) devices.get("requestPath");
        List<Map<String, Object>> urls = (List<Map<String, Object>>) devices.get("devices");
        final ObservableList<DeviceStatusVO> devicesVO = tableView.getItems();
        devicesVO.clear();
        if (urls.isEmpty()) {
            devicesVO.add(new DeviceStatusVO("", "", "Não foi encontrado nenhum aparelho cadastrado.", ""));
            return;
        }

        Thread thread = new Thread(() -> {
            for (int i = 0; i < urls.size(); i++) {
                Map<String, Object> u = urls.get(i);
                DeviceStatusVO device =
                        new DeviceStatusVO((String) u.get("ip"), (String) u.get("nome"), "Verificando...", (String) u.get("url"));
                Platform.runLater(() -> devicesVO.add(device));

                final StringBuilder status = new StringBuilder();
                try {
                    deviceService.post(device.getUrl(), "application/json", requestPath, null);
                    status.append("OK");
                } catch (Exception e) {
                    status.append("Erro: " + e.getMessage());
                }
                Platform.runLater(() -> device.setStatus(status.toString()));

                if (i == urls.size() - 1) {
                    Platform.runLater(() -> tableView.refresh());
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    public void loadHome(ActionEvent event) throws IOException {
        Parent page = ClientApplication.getInstance().fxmlLoader("/fxml/home.fxml");
        tableView.visibleProperty().bind(javafx.beans.binding.Bindings.isEmpty(tableView.getItems()).not());
        tableView.setFixedCellSize(24d);
        tableView.prefHeightProperty().bind(tableView.fixedCellSizeProperty().multiply(12).add(1.1));
        tableView.minHeightProperty().bind(tableView.prefHeightProperty());
        tableView.maxHeightProperty().bind(tableView.prefHeightProperty());
        Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        currentStage.setScene(new Scene(page, 823, 600));
        currentStage.show();
    }
}
