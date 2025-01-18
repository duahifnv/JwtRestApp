package study.jwt.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FlywayMigrationConfig {
    private final Flyway flyway;
    @PostConstruct
    public void cleanMigrationStrategy() {
        flyway.clean();
        flyway.baseline();
        flyway.migrate();
    }
}
