package com.example.config;

import com.example.doma.jdbc.UnknownColumnIgnoreHandler;
import org.apache.commons.lang3.StringUtils;
import org.seasar.doma.boot.autoconfigure.DomaAutoConfiguration;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.GreedyCacheSqlFileRepository;
import org.seasar.doma.jdbc.NoCacheSqlFileRepository;
import org.seasar.doma.jdbc.SqlFileRepository;
import org.seasar.doma.jdbc.UnknownColumnHandler;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import javax.sql.DataSource;

@Configuration
public class DomaConfig implements Config {

  private final Environment environment;

  private final DomaAutoConfiguration domaAutoConfiguration;

  private final DataSource dataSource;

  private SqlFileRepository sqlFileRepository;

  private final String springProfilesActive;

  public DomaConfig(
      DomaAutoConfiguration domaAutoConfiguration,
      DataSource dataSource,
      Environment environment,
      @Value("${spring.profiles.active}") String springProfilesActive) {
    this.springProfilesActive = springProfilesActive;
    this.domaAutoConfiguration = domaAutoConfiguration;
    this.environment = environment;
    this.dataSource = dataSource;
  }

  @Override
  public DataSource getDataSource() {
    return new TransactionAwareDataSourceProxy(dataSource);
  }

  @Override
  public UnknownColumnHandler getUnknownColumnHandler() {
    if (StringUtils.equals(springProfilesActive, "develop")) {
      return domaAutoConfiguration.domaConfigBuilder().unknownColumnHandler();
    }
    return new UnknownColumnIgnoreHandler();
  }

  @Override
  public Dialect getDialect() {
    return domaAutoConfiguration.dialect(environment);
  }

  @Autowired
  public void setSqlFileRepository() {
    // develop モードの時は SQL ファイルがキャッシュされないようにする
    if (StringUtils.equals(springProfilesActive, "develop")) {
      sqlFileRepository = new NoCacheSqlFileRepository();
    } else {
      sqlFileRepository = new GreedyCacheSqlFileRepository();
    }
  }

  @Override
  public SqlFileRepository getSqlFileRepository() {
    return sqlFileRepository;
  }
}
