package facetec.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by rkogawa on 04/05/19.
 */
@SpringBootApplication
@EnableCaching(proxyTargetClass = true)
@EnableTransactionManagement(proxyTargetClass = true)
@EnableJpaRepositories
@EntityScan({ "facetec" })
@ComponentScan({ "facetec" })
@EnableAutoConfiguration(exclude = { org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration.class })
@EnableScheduling
public class Application extends SpringBootServletInitializer {

    static {
        System.setProperty("spring.liquibase.changeLog", "classpath:/liquibase/db.changelog.yaml");
        System.setProperty("spring.servlet.multipart.max-file-size", "10MB");
        System.setProperty("spring.servlet.multipart.max-request-size", "10MB");
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(Application.class);
    }

}
