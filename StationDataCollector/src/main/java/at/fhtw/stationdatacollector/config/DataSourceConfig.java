package at.fhtw.stationdatacollector.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean(name="Db1")
    public JdbcTemplate jdbcTemplate1(){
        HikariConfig hikariConfig=new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:postgresql://localhost:30011/stationdb");
        hikariConfig.setUsername("postgres");
        hikariConfig.setPassword("postgres");
        DataSource dataSource=new HikariDataSource(hikariConfig);
        return new JdbcTemplate(dataSource);
    }
    @Bean(name="Db2")
    public JdbcTemplate jdbcTemplate2(){
        HikariConfig hikariConfig=new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:postgresql://localhost:30012/stationdb");
        hikariConfig.setUsername("postgres");
        hikariConfig.setPassword("postgres");
        DataSource dataSource=new HikariDataSource(hikariConfig);
        return new JdbcTemplate(dataSource);
    }
    @Bean(name="Db3")
    public JdbcTemplate jdbcTemplate3(){
        HikariConfig hikariConfig=new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:postgresql://localhost:30013/stationdb");
        hikariConfig.setUsername("postgres");
        hikariConfig.setPassword("postgres");
        DataSource dataSource=new HikariDataSource(hikariConfig);
        return new JdbcTemplate(dataSource);
    }

}
// Get IP of the containers
// docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' semesterproject-station-1-db-1
// docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' semesterproject-station-2-db-1
// docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' semesterproject-station-3-db-1