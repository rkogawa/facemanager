package facetec.client;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;

import javax.swing.*;
import java.awt.*;
import java.net.URI;

/**
 * Created by rkogawa on 15/05/19.
 */
@SpringBootApplication
@ComponentScan({ "facetec" })
public class ClientApplication implements CommandLineRunner {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ClientApplication.class).headless(false).run(args);
    }

    @Override
    public void run(String... args) {
        JFrame frame = new JFrame("Spring Boot Swing App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 300);
        JPanel panel = new JPanel(new BorderLayout());
        JTextField text = new JTextField("Spring Boot can be used with Swing apps");
        panel.add(text, BorderLayout.CENTER);
        frame.setContentPane(panel);
        frame.setVisible(true);

        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.browse(new URI("https://ec2-18-223-252-184.us-east-2.compute.amazonaws.com:8443/facetec/"));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

}
