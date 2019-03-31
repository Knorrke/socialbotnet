package config;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.datasource.init.ScriptUtils;

@Configuration
public class DatabaseConfig {
  /** Set to true to use in-memory database * */
  private static boolean DEBUG_MODE = false;

  static final Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);

  @Bean
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
      if (DEBUG_MODE) {
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        EmbeddedDatabase db =
            builder
                .setType(EmbeddedDatabaseType.HSQL)
                .addScript("sql/create-db.sql")
                .addScript("sql/insert-data.sql")
                .build();
        return db;
      } else {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.hsqldb.jdbcDriver");
        dataSource.setUrl("jdbc:hsqldb:file:database/database");
        dataSource.setUsername("SA");
        dataSource.setPassword("");
        createSchemeIfNotExists(dataSource, "sql/create-db.sql");
        return dataSource;
      }
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

  /**
   * Sets the debug mode (default false). To enable debug database call this before creating the
   * database.
   *
   * @param debug
   */
  public static void setDebugMode(boolean debug) {
    DEBUG_MODE = debug;
  }
}
