package config;

import io.zonky.test.db.postgres.embedded.EmbeddedPostgres;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

@Configuration
public class DatabaseConfig {
  static final Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);

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
      createSchemeIfNotExists(basicDataSource, "sql/create-db-postgresql.sql");
      return basicDataSource;
    } else {
      DataSource embeddedPostgresDS = EmbeddedPostgres.builder().start().getPostgresDatabase();
      return embeddedPostgresDS;
    }
  }

  private void createSchemeIfNotExists(DataSource dataSource, String sqlScriptPath)
      throws SQLException {
    try (Connection connection = dataSource.getConnection()) {
      // check if table post exists
      ResultSet rs = connection.getMetaData().getTables(null, null, "%", new String[] {"TABLE"});
      boolean initialized = false;
      while (rs.next() && !initialized) {
        if (rs.getString("TABLE_NAME").equalsIgnoreCase("post")) {
          logger.info("Loading existing database");
          initialized = true;
        }
      }
      if (!initialized) {
        logger.info("Initializing database");
        Resource create = new ClassPathResource(sqlScriptPath);
        ScriptUtils.executeSqlScript(connection, create);
      }
    }
  }
}
