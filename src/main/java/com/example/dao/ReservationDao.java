package com.example.dao;

import com.example.entity.Reservation;
import com.example.entity.ReservationId;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.List;
import java.util.Optional;

/**
 * 予約エンティティのデータアクセスオブジェクト（DAO）。
 * 
 * <p>このインターフェースはDoma2の2-way SQLを使用して、
 * 予約テーブルへのCRUD操作を提供します。
 * SQLファイルは{@code META-INF/com/example/dao/ReservationDao/}ディレクトリに配置され、
 * メソッド名と対応するSQLファイルが自動的にマッピングされます。
 * </p>
 * 
 * <h3>SQLファイルマッピング</h3>
 * <ul>
 * <li>{@code selectById} → selectById.sql</li>
 * <li>{@code selectAll} → selectAll.sql</li>
 * <li>{@code insert} → 自動生成SQL</li>
 * <li>{@code update} → 自動生成SQL</li>
 * </ul>
 * 
 * <p>このDAOは{@code @ConfigAutowireable}により、
 * Spring BootのDomaAutoConfigurationで自動的にBeanとして登録されます。
 * </p>
 */
@ConfigAutowireable
@Dao
public interface ReservationDao {

    /**
     * 指定されたIDで予約を検索します。
     * 
     * <p>対応するSQLファイル: {@code selectById.sql}</p>
     *
     * @param id 検索する予約ID
     * @return 予約エンティティ（存在しない場合は空のOptional）
     */
    @Select
    Optional<Reservation> selectById(ReservationId id);

    /**
     * 全ての予約を取得します。
     * 
     * <p>対応するSQLファイル: {@code selectAll.sql}
     * 結果は名前順でソートされます。</p>
     *
     * @return 予約エンティティのリスト（空の場合は空のリスト）
     */
    @Select
    List<Reservation> selectAll();

    /**
     * 新しい予約を挿入します。
     * 
     * <p>SQLは自動生成され、IDは{@code IDENTITY}戦略により
     * データベースで自動採番されます。</p>
     *
     * @param reservation 挿入する予約エンティティ
     * @return 影響を受けた行数（通常は1）
     */
    @Insert
    int insert(Reservation reservation);

    /**
     * 既存の予約を更新します。
     * 
     * <p>SQLは自動生成され、IDに基づいて対象レコードが特定されます。</p>
     *
     * @param reservation 更新する予約エンティティ
     * @return 影響を受けた行数（レコードが存在する場合は1、存在しない場合は0）
     */
    @Update
    int update(Reservation reservation);
}
