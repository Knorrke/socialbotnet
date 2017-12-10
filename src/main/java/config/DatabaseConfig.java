package config;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.ScriptException;
import org.springframework.jdbc.datasource.init.ScriptUtils;

@Configuration
public class DatabaseConfig {

	@Bean
	public DataSource dataSource() throws Exception {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("org.hsqldb.jdbcDriver");
        dataSource.setUrl("jdbc:hsqldb:file:src/main/resources/database/database");
        dataSource.setUsername("SA");
        dataSource.setPassword("");
/*        Resource create = new ClassPathResource("sql/create-db.sql");
        Resource insert = new ClassPathResource("sql/insert-data.sql");
        ScriptUtils.executeSqlScript(dataSource.getConnection(), create);
        ScriptUtils.executeSqlScript(dataSource.getConnection(), insert);*/
        return dataSource;
	}

}
