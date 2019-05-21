package facetec.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Value;
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
    private ConfigurableApplicationContext springContext;

    private Parent rootNode;

    private FXMLLoader fxmlLoader;

    @Value("${facetec.client.url:https://www.facetec.tk/}")
    private String url;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void run(String... args) {
        getHostServices().showDocument(url);
    }

    @Override
    public void init() throws Exception {
        springContext = new SpringApplicationBuilder(ClientApplication.class).headless(true).run();
        fxmlLoader = new FXMLLoader();
        fxmlLoader.setControllerFactory(springContext::getBean);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        fxmlLoader.setLocation(getClass().getResource("/fxml/sample.fxml"));
        rootNode = fxmlLoader.load();

        primaryStage.getIcons().add(new Image("facetec_logo_branco.jpg"));

        primaryStage.setTitle("FACETEC - Interface FTCA-888");
        Scene scene = new Scene(rootNode, 600, 420);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        springContext.stop();
        Platform.exit();
        System.exit(0);
    }
}
