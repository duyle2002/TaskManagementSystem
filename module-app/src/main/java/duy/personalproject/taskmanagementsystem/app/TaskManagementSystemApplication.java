package duy.personalproject.taskmanagementsystem.app;

import duy.personalproject.taskmanagementsystem.auth.config.properties.JwtConfigProperties;
import duy.personalproject.taskmanagementsystem.auth.config.properties.RefreshTokenConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableConfigurationProperties({JwtConfigProperties.class, RefreshTokenConfigProperties.class})
@EnableScheduling
@EnableJpaAuditing
@EnableTransactionManagement
public class TaskManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskManagementSystemApplication.class, args);
    }

}

@Component
class StartupListener {

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        String serverUrl = "http://localhost:8080/";
        String swaggerUrl = "http://localhost:8080/swagger-ui/index.html";

        System.out.println("\n" +
                "╔════════════════════════════════════════════════════════════════╗\n" +
                "║         🚀 Task Management System Started Successfully 🚀      ║\n" +
                "╠════════════════════════════════════════════════════════════════╣\n" +
                "║  📌 Server: " + String.format("%-47s", serverUrl) + "║\n" +
                "║  📚 Swagger UI: " + String.format("%-41s", swaggerUrl) + "║\n" +
                "╚════════════════════════════════════════════════════════════════╝\n");
    }
}

