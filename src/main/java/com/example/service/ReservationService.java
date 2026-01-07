package com.example.service;

import com.example.dao.ReservationDao;
import com.example.entity.Reservation;
import com.example.entity.ReservationId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * 予約管理のビジネスロジックを提供するサービスクラス。
 *
 * <p>このサービスは予約エンティティの操作を管理し、適切なトランザクション境界を提供します。
 * 複合操作（save）では、内部的に{@link ReservationServiceHelper}を使用して、
 * ネストしたトランザクションによる独立した操作を実現しています。
 * </p>
 *
 * <h3>トランザクション設計</h3>
 * <ul>
 * <li>{@code create} - 標準的なトランザクション境界</li>
 * <li>{@code save} - トランザクションなし（内部サービスに委譲）</li>
 * <li>{@code findById} - 読み取り専用（トランザクションなし）</li>
 * </ul>
 */
@Service
public class ReservationService {

    private static final Logger logger = LoggerFactory.getLogger(ReservationService.class);

    private final ReservationDao dao;
    private final ReservationServiceHelper helper;

    /**
     * ReservationServiceを構築します。
     *
     * @param dao    DAO層への参照
     * @param helper ネストトランザクション用の内部サービス
     */
    ReservationService(ReservationDao dao, ReservationServiceHelper helper) {
        this.dao = dao;
        this.helper = helper;
    }

    /**
     * 指定されたIDで予約を検索します。
     *
     * @param id 予約ID
     * @return 予約エンティティ（存在しない場合は空のOptional）
     */
    public Optional<Reservation> findById(ReservationId id) {
        return dao.selectById(id);
    }

    /**
     * 新しい予約を作成します。
     *
     * <p>このメソッドは標準的なトランザクション境界で実行されます。
     *
     * @param entity 作成する予約エンティティ
     * @return 影響を受けた行数
     */
    @Transactional
    public int create(Reservation entity) {
        return dao.insert(entity);
    }

    /**
     * 予約を保存します（作成または更新）。
     *
     * <p>このメソッドはエンティティのIDの有無により動作を切り替えます：
     * <ul>
     * <li>ID が null の場合: 新規作成（{@link ReservationServiceHelper#create}）</li>
     * <li>ID が設定済みの場合: 更新（{@link ReservationServiceHelper#update}）</li>
     * </ul>
     *
     * <p>実際のDB操作は{@link ReservationServiceHelper}に委譲され、
     * ネストトランザクション（PROPAGATION_NESTED）で実行されます。
     * これにより、save操作自体は独立したトランザクション境界を持たず、
     * 呼び出し元のトランザクション制御に柔軟性を提供します。
     *
     * @param entity 保存する予約エンティティ
     * @return 影響を受けた行数
     */
    public int save(Reservation entity) {
        if (entity.getId() == null) {
            return helper.create(entity);
        } else {
            return helper.update(entity);
        }
    }


}
