package com.example.config;

import com.example.doma.jdbc.UnknownColumnIgnoreHandler;
import org.seasar.doma.boot.autoconfigure.DomaAutoConfiguration;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.GreedyCacheSqlFileRepository;
import org.seasar.doma.jdbc.NoCacheSqlFileRepository;
import org.seasar.doma.jdbc.SqlFileRepository;
import org.seasar.doma.jdbc.UnknownColumnHandler;
import org.seasar.doma.jdbc.dialect.Dialect;
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
  private final String domaDialect;

  public DomaConfig(
      DomaAutoConfiguration domaAutoConfiguration,
      DataSource dataSource,
      Environment environment,
      @Value("${spring.profiles.active:}") String springProfilesActive,
      @Value("${doma.dialect:postgres}") String domaDialect) {
    this.springProfilesActive = springProfilesActive;
    this.domaDialect = domaDialect;
    this.domaAutoConfiguration = domaAutoConfiguration;
    this.environment = environment;
    this.dataSource = dataSource;
    setSqlFileRepository();
  }

  @Override
  public DataSource getDataSource() {
    return new TransactionAwareDataSourceProxy(dataSource);
  }

  @Override
  public UnknownColumnHandler getUnknownColumnHandler() {
    if ("devel".equals(springProfilesActive)) {
      return domaAutoConfiguration.domaConfigBuilder().unknownColumnHandler();
    }
    return new UnknownColumnIgnoreHandler();
  }

  @Override
  public Dialect getDialect() {
    String dialectName = domaDialect.toLowerCase();
    if ("postgres".equals(dialectName) || "postgresql".equals(dialectName)) {
      return new org.seasar.doma.jdbc.dialect.PostgresDialect();
    } else if ("h2".equals(dialectName)) {
      return new org.seasar.doma.jdbc.dialect.H2Dialect();
    } else {
      // fallback to PostgreSQL
      return new org.seasar.doma.jdbc.dialect.PostgresDialect();
    }
  }

  public void setSqlFileRepository() {
    // develop モードの時は SQL ファイルがキャッシュされないようにする
    if ("devel".equals(springProfilesActive)) {
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
