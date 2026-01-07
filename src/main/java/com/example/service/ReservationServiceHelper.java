package com.example.service;

import com.example.dao.ReservationDao;
import com.example.entity.Reservation;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 予約操作のためのネストトランザクション専用の内部サービス。
 *
 * <p>このサービスは{@link ReservationService}から呼び出され、
 * ネストトランザクション（{@code PROPAGATION_NESTED}）を使用して、
 * 外部のトランザクション境界とは独立した操作を提供します。
 * </p>
 *
 * <h3>ネストトランザクションの利点</h3>
 * <ul>
 * <li>部分的なロールバック: 内部操作の失敗が外部トランザクションに影響しない</li>
 * <li>独立性: 外部のトランザクション状態に関係なく新しいトランザクションを開始</li>
 * <li>柔軟性: 呼び出し元が異なるトランザクション戦略を選択可能</li>
 * </ul>
 *
 * <p><strong>注意:</strong> このクラスはpackage-privateであり、
 * 同一パッケージ内の{@link ReservationService}からのみアクセス可能です。
 * </p>
 */
@Component
class ReservationServiceHelper {

    private final ReservationDao dao;

    /**
     * ReservationInternalServiceを構築します。
     *
     * @param dao DAO層への参照
     */
    ReservationServiceHelper(ReservationDao dao) {
        this.dao = dao;
    }

    /**
     * 新しい予約をネストトランザクション内で作成します。
     *
     * <p>このメソッドは{@code PROPAGATION_NESTED}を使用して実行され、
     * 呼び出し元のトランザクションから独立したセーブポイントを作成します。
     * 作成操作が失敗した場合、このメソッドのみがロールバックされ、
     * 外部のトランザクションには影響しません。
     * </p>
     *
     * @param entity 作成する予約エンティティ
     * @return 影響を受けた行数
     * @throws org.springframework.dao.DataAccessException データベースアクセスエラーが発生した場合
     */
    @Transactional(propagation = Propagation.NESTED)
    public int create(Reservation entity) {
        return dao.insert(entity);
    }

    /**
     * 既存の予約をネストトランザクション内で更新します。
     *
     * <p>このメソッドは{@code PROPAGATION_NESTED}を使用して実行され、
     * 呼び出し元のトランザクションから独立したセーブポイントを作成します。
     * 更新操作が失敗した場合、このメソッドのみがロールバックされ、
     * 外部のトランザクションには影響しません。
     * </p>
     *
     * @param entity 更新する予約エンティティ
     * @return 影響を受けた行数
     * @throws org.springframework.dao.DataAccessException データベースアクセスエラーが発生した場合
     */
    @Transactional(propagation = Propagation.NESTED)
    public int update(Reservation entity) {
        return dao.update(entity);
    }
}
