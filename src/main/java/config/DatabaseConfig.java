package config;

import io.zonky.test.db.postgres.embedded.EmbeddedPostgres;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class DatabaseConfig {
  @Bean
  @Primary
  public DataSource dataSource() throws Exception {
    if (System.getenv("JDBC_DATABASE_URL") != null) {
      String dbUrl = System.getenv("JDBC_DATABASE_URL");
      String username = System.getenv("JDBC_DATABASE_USERNAME");
      String password = System.getenv("JDBC_DATABASE_PASSWORD");

      BasicDataSource basicDataSource = new BasicDataSource();
      basicDataSource.setDriverClassName("org.postgresql.Driver");
      basicDataSource.setUrl(dbUrl);
      basicDataSource.setUsername(username);
      basicDataSource.setPassword(password);
      return basicDataSource;
    } else {
      return EmbeddedPostgres.builder().start().getPostgresDatabase();
    }
  }
}
