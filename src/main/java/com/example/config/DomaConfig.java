package com.example.config;

import com.example.doma.jdbc.UnknownColumnIgnoreHandler;
import org.seasar.doma.jdbc.GreedyCacheSqlFileRepository;
import org.seasar.doma.jdbc.NoCacheSqlFileRepository;
import org.seasar.doma.jdbc.SqlFileRepository;
import org.seasar.doma.jdbc.UnknownColumnHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Doma2のカスタム設定クラス。
 * 
 * <p>Spring Boot DomaのAutoConfigurationを活用しつつ、
 * 環境に応じたカスタム設定を提供します。
 */
@Configuration
public class DomaConfig {

  /**
   * SQLファイルリポジトリのBean定義。
   * 
   * <p>開発環境ではSQLファイルのキャッシュを無効にし、
   * 本番環境ではキャッシュを有効にします。
   *
   * @param springProfilesActive アクティブなSpringプロファイル
   * @return SQLファイルリポジトリの実装
   */
  @Bean
  public SqlFileRepository sqlFileRepository(@Value("${spring.profiles.active:}") String springProfilesActive) {
    // develop モードの時は SQL ファイルがキャッシュされないようにする
    if ("devel".equals(springProfilesActive)) {
      return new NoCacheSqlFileRepository();
    } else {
      return new GreedyCacheSqlFileRepository();
    }
  }

  /**
   * 未知のカラムハンドラーのBean定義。
   * 
   * <p>本番環境でのみ有効になり、未知のカラムを無視する
   * カスタムハンドラーを提供します。
   * 
   * <p>開発環境では、Spring Boot DomaのAutoConfigurationが提供する
   * デフォルトハンドラー（例外を投げる）が使用されます。
   *
   * @return {@link UnknownColumnIgnoreHandler}
   */
  @Bean
  @ConditionalOnExpression("!'devel'.equals('${spring.profiles.active:}')")
  public UnknownColumnHandler unknownColumnHandler() {
    return new UnknownColumnIgnoreHandler();
  }
}