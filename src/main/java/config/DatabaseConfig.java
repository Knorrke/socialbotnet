package config;

import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

@Configuration
public class DatabaseConfig {

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

      return basicDataSource;
    } else {
      EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
      EmbeddedDatabase db =
          builder
              .setType(EmbeddedDatabaseType.HSQL)
              .addScript("sql/create-db.sql")
              .addScript("sql/insert-data.sql")
              .build();
      return db;
    }

    //		DriverManagerDataSource dataSource = new DriverManagerDataSource();
    //		dataSource.setDriverClassName("org.hsqldb.jdbcDriver");
    //        dataSource.setUrl("jdbc:hsqldb:file:src/main/resources/database/database");
    //        dataSource.setUsername("SA");
    //        dataSource.setPassword("");
    /*        Resource create = new ClassPathResource("sql/create-db.sql");
    Resource insert = new ClassPathResource("sql/insert-data.sql");
    ScriptUtils.executeSqlScript(dataSource.getConnection(), create);
    ScriptUtils.executeSqlScript(dataSource.getConnection(), insert);*/
    //        return dataSource;
  }
}
