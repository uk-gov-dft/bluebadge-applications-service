package uk.gov.service.bluebadge.test.utils;

import java.util.List;
import java.util.Map;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

@Slf4j
public class DbUtils {
  private final JdbcTemplate jdbc;

  public DbUtils(Map<String, Object> config) {
    String url = (String) config.get("url");
    String username = (String) config.get("username");
    String  ***REMOVED***);
    String driver = (String) config.get("driverClassName");
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName(driver);
    dataSource.setUrl(url);
    dataSource.setUsername(username);
    dataSource.setPassword(password);
    jdbc = new JdbcTemplate(dataSource);
    log.info("init jdbc template: {}", url);
  }

  @SneakyThrows
  public boolean runScript(String script) {
    log.info("Karate DB. Running script: {}", script);
    ScriptUtils.executeSqlScript(
        jdbc.getDataSource().getConnection(), new ClassPathResource(script));
    return true;
  }

  public Object readValue(String query) {
    return jdbc.queryForObject(query, Object.class);
  }

  public Map<String, Object> readRow(String query) {
    log.debug("Karate DB query: {}", query);
    Map<String, Object> stringObjectMap = jdbc.queryForMap(query);
    log.debug("Karate DB result: {}", stringObjectMap);
    return stringObjectMap;
  }

  public List<Map<String, Object>> readRows(String query) {
    return jdbc.queryForList(query);
  }
}
