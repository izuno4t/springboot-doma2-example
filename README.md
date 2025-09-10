# SpringBoot + Doma2 Example Application

SpringBootでDoma2を利用してRDBMS（PostgreSQL、MySQLなど）を利用する実装例です。

---

## このプロジェクトの特徴

### Doma2の拡張
- 未知カラム無視ハンドラの提供: `UnknownColumnIgnoreHandler`（`com.example.doma.jdbc.UnknownColumnIgnoreHandler`）。
  - `DomaConfig`で、本番系（`spring.profiles.active` が `devel` 以外）でのみ有効化し、未知カラムを無視して安全に運用。
- SQLファイルのキャッシュ戦略をプロファイルで切替: `devel` プロファイルは即時反映のため`NoCacheSqlFileRepository`、それ以外は`GreedyCacheSqlFileRepository`。
- `doma-spring-boot-starter`を基盤に、必要最小限のBeanを追加・上書きして運用特性（ハンドラ/キャッシュ）を調整。

### Doma2導入・SpringBoot連携
- `doma-spring-boot-starter`で自動設定を利用。DaoはDIで注入、`@Transactional`でTx制御、接続は`application.properties`で管理。
- `config/DomaConfig.java`で必要に応じてDialect/Repository/HandlerなどのBeanを定義し、AutoConfigurationの上に最小限のカスタマイズを適用。

---

## 必要環境
- Java 21
- Docker / Docker Compose（PostgreSQL用）
- Maven 3.6+（`./mvnw`推奨）

## データベースセットアップ手順
```bash
# logディレクトリ作成（権限設定必須）
sudo rm -rf log && sudo mkdir -p log/postgres && sudo chown -R $(whoami):$(id -gn) log

# PostgreSQL起動（初期化に約15秒）
docker compose up -d
sleep 15

# スキーマ作成
PGPASSWORD=example psql -h localhost -U example -d example -f schema/create_table.sql

# テーブル確認
PGPASSWORD=example psql -h localhost -U example -d example -c "\\d reservation;"
```

## 参考リンク

- [domaframework / doma-spring-boot](https://github.com/domaframework/doma-spring-boot)
- [Spring Boot で Doma 2 を使用するには](https://ksby.hatenablog.com/entry/2015/10/15/043336)
- [ksby / springboot-doma2-template](https://github.com/ksby/springboot-doma2-template)
- [naokism / doma2-spring-boot-multiple-ds](https://github.com/naokism/doma2-spring-boot-multiple-ds)
- [Spring Boot2 x Doma2のハマったこと備忘録](https://qiita.com/kobayo/items/4226c40d454336eadacd)
- [Spring Boot + Doma2 で UnknownColumnHandler を設定する](https://qiita.com/yanagin/items/99f62acbd2e5b9ca8f98)

