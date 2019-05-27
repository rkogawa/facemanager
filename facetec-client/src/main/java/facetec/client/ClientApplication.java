package facetec.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by rkogawa on 15/05/19.
 */
@SpringBootApplication
@ComponentScan({ "facetec" })
@EnableScheduling
public class ClientApplication extends Application implements CommandLineRunner {

    private static ConfigurableApplicationContext springContext;

    private static Parent rootNode;

    private static ClientApplication app;

    public ClientApplication() {
        app = this;
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static ClientApplication getInstance() {
        return app;
    }

    @Override
    public void run(String... args) {
    }

    @Override
    public void init() throws Exception {
        springContext = new SpringApplicationBuilder(ClientApplication.class).headless(true).run();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        rootNode = fxmlLoader("/fxml/login.fxml");

        primaryStage.getIcons().add(new Image("facetec_logo_branco.jpg"));
        primaryStage.setTitle("FACETEC - Interface FTCA-888");
        Scene scene = new Scene(rootNode, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public Parent fxmlLoader(String resource) throws java.io.IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setControllerFactory(springContext::getBean);
        fxmlLoader.setLocation(getClass().getResource(resource));
        return fxmlLoader.load();
    }

    @Override
    public void stop() throws Exception {
        springContext.stop();
        Platform.exit();
        System.exit(0);
    }

}
