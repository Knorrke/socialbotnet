package config;

import jakarta.annotation.PostConstruct;
import javax.sql.DataSource;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Migrate {

  @Autowired DataSource ds;

  @PostConstruct
  public void migrateWithFlyway() {
    Flyway flyway =
        Flyway.configure().dataSource(ds).locations("db/migration").baselineOnMigrate(true).load();

    flyway.migrate();
  }
}
