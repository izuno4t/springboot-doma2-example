# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Spring Boot + Doma2 ORM の参考実装。Java 25 / Spring Boot 4.0.6 / Doma2 3.11.1 / PostgreSQL 構成で、`doma-spring-boot-starter` を基盤にして運用特性（未知カラム無視、SQL キャッシュ）をプロファイルで切り替える。

関連ドキュメント:
- `README.md` — 機能概要、依存更新コマンド、参考リンク
- `AGENTS.md` — リポジトリ作業規約（**作業中のやりとりは日本語**、`pom.xml` の安易な変更禁止、複数案の提示と Pros/Cons など）
- `SECURITY.md` — 脆弱性報告手順

## Development Commands

### テスト実行（Docker 必要、DB 起動は不要）
```bash
./mvnw test                                      # 全テスト（Testcontainers が PostgreSQL を自動起動）
./mvnw test -Dtest=ReservationDaoTest            # クラス単位
./mvnw test -Dtest=ReservationServiceTest$Save#データがないのでinsertされる  # メソッド単位
./mvnw verify                                    # テスト + 静的解析（Checkstyle / PMD / SpotBugs / JaCoCo）
./mvnw verify -DskipTests                        # 静的解析のみ
```

### アプリ起動用ローカル DB セットアップ（テストには不要）
`./mvnw spring-boot:run` でアプリを起動したいときのみ実施。
```bash
sudo rm -rf log && sudo mkdir -p log/postgres && sudo chown -R $(whoami):$(id -gn) log
docker compose up -d   # docker/postgres/Dockerfile (postgres:17 ベース)
sleep 15
PGPASSWORD=example psql -h localhost -U example -d example -f schema/create_table.sql
```
接続: `jdbc:postgresql://localhost:5432/example` / `example` / `example`

### 依存更新
```bash
./mvnw versions:display-dependency-updates       # 更新候補確認
./mvnw versions:set-property -Dproperty=spring-boot.version -DnewVersion=X.Y.Z
```

## Architecture

### Doma2 統合の要点
- `config/DomaConfig.java` で **プロファイルにより Bean を切替**:
  - `devel` プロファイル: `NoCacheSqlFileRepository`（SQL ファイル即時反映）、`UnknownColumnHandler` は Bean 定義せず AutoConfiguration のデフォルト（例外を投げる）を使う
  - それ以外（本番想定）: `GreedyCacheSqlFileRepository`、`UnknownColumnIgnoreHandler`（未知カラムを無視）
- 2-way SQL は `src/main/resources/META-INF/<DAO完全修飾名>/<メソッド名>.sql` のパス規約で配置
- `ReservationId` は値オブジェクトで型安全な ID を提供
- アノテーションプロセッサは `maven-compiler-plugin` で明示設定（Java 25 互換性のため必須）

### トランザクション設計（重要）
`ReservationService` は **NESTED 伝播** を使う特殊な構造:
- `ReservationService.save()` 自体は `@Transactional` を付けない
- 実 DB 操作は package-private な `ReservationServiceHelper.create() / update()` に委譲され、それぞれ `@Transactional(propagation = NESTED)` を持つ
- 目的: 外側のトランザクション境界から独立したセーブポイントを設け、部分ロールバックを可能にする
- `ReservationService.create()` は通常の `@Transactional`、`findById()` はトランザクションなし

### テスト構成
- `TestConfig` が `@EnableAutoConfiguration` + `@ComponentScan(com.example)` + `TestContainersConfig` を `@Import`
- `TestContainersConfig` は `postgres:16-alpine` を `@ServiceConnection` で公開、Spring Boot が自動的に DataSource を構成
- `src/test/resources/application.properties` で `spring.sql.init.schema-locations=file:./schema/create_table.sql` を指定し、コンテナ起動後にスキーマを自動投入
- テストプロファイルも `devel` なので、SQL ファイル変更は再ビルドなしで反映され、未知カラムはエラーになる

### プロファイル早見表
| プロファイル | SQL キャッシュ | 未知カラム | 用途 |
|---|---|---|---|
| `devel`（テスト / ローカル起動のデフォルト） | 無効 | 例外 | 開発中の即時反映 |
| その他 | 有効（Greedy） | 無視 | 本番想定 |

## Known Constraints

- `./mvnw spring-boot:run` はデフォルトプロファイル（`application.properties` で `devel`）で DataSource 設定が必要。DB 未起動だと "Failed to determine a suitable driver class" で停止する。コンソールアプリで Web サーバーは持たない
- 静的解析の警告は非ファタル設定: PMD は ASM 互換性警告、SpotBugs は medium 優先度の指摘あり。`./mvnw verify` 自体は通る（JaCoCo の 90% ブランチカバレッジは integration test 用、現状 integration test なしで適用されない）
- CI は `.github/workflows/ci.yml`（テスト）と `qodana_code_quality.yml`（品質チェック）の 2 本
