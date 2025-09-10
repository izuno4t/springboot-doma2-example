package com.example.entity;

import org.jetbrains.annotations.NotNull;
import org.seasar.doma.Domain;

import java.util.Objects;

/**
 * 予約IDを表す値オブジェクト。
 * <p>
 * 整数のID値をラップし、nullは許容しません。
 * </p>
 */
@Domain(valueType = Integer.class, accessorMethod = "value", factoryMethod = "of")
public record ReservationId(Integer value) {

    /**
     * 指定された非nullの値でReservationIdを生成します。
     *
     * @param value ID値（null不可）
     * @throws NullPointerException valueがnullの場合
     */
    public ReservationId {
        Objects.requireNonNull(value, "value must not be null");
    }

    /**
     * 整数値からReservationIdを生成します。
     *
     * @param value ID値（null不可）
     * @return 指定値を持つReservationId
     * @throws NullPointerException valueがnullの場合
     */
    public static ReservationId of(Integer value) {
        Objects.requireNonNull(value, "value must not be null");
        return new ReservationId(value);
    }

    /**
     * 内部のID値を返します（nullはありません）。
     *
     * @return ID値（nullなし）
     */
    @Override
    public Integer value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReservationId that = (ReservationId) o;
        return Objects.equals(value, that.value);
    }

    /**
     * このオブジェクトの文字列表現を返します。
     *
     * @return クラス名とID値を含む文字列
     */
    @Override
    public @NotNull String toString() {
        return "ReservationId{" +
                "value=" + value +
                '}';
    }
}