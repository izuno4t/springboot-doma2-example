package com.example.entity;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests for ReservationId value object.
 */
class ReservationIdTest {

    @Nested
    class Constructor {

        @Test
        void 値を指定してReservationIdを作成できる() {
            var id = new ReservationId(123);
            assertThat(id.value()).isEqualTo(123);
        }

        @Test
        void nullはコンストラクタで例外() {
            assertThatThrownBy(() -> new ReservationId(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("value must not be null");
        }
    }

    @Nested
    class Of {

        @Test
        void 正の値からReservationIdを作成できる() {
            var id = ReservationId.of(456);
            assertThat(id).isNotNull();
            assertThat(id.value()).isEqualTo(456);
        }

        @Test
        void nullはofで例外() {
            assertThatThrownBy(() -> ReservationId.of(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("value must not be null");
        }

        @Test
        void ゼロからReservationIdを作成できる() {
            var id = ReservationId.of(0);
            assertThat(id).isNotNull();
            assertThat(id.value()).isEqualTo(0);
        }
    }

    @Nested
    class Equals {

        @Test
        void 同じ値のReservationIdは等しい() {
            var id1 = new ReservationId(123);
            var id2 = new ReservationId(123);
            assertThat(id1).isEqualTo(id2);
        }

        @Test
        void 異なる値のReservationIdは等しくない() {
            var id1 = new ReservationId(123);
            var id2 = new ReservationId(456);
            assertThat(id1).isNotEqualTo(id2);
        }

        @Test
        void 自分自身と等しい() {
            var id = new ReservationId(123);
            assertThat(id).isSameAs(id);
        }

        @Test
        void nullとは等しくない() {
            var id = new ReservationId(123);
            assertThat(id).isNotEqualTo(null);
        }

        @Test
        void 他の型とは等しくない() {
            var id = new ReservationId(123);
            assertThat(id).isNotEqualTo("123");
        }
    }

    @Nested
    class HashCode {

        @Test
        void 同じ値のReservationIdは同じハッシュコードを持つ() {
            var id1 = new ReservationId(123);
            var id2 = new ReservationId(123);
            assertThat(id1.hashCode()).isEqualTo(id2.hashCode());
        }
    }

    @Nested
    class ToString {

        @Test
        void toString_正常な値() {
            var id = new ReservationId(123);
            assertThat(id.toString()).isEqualTo("ReservationId{value=123}");
        }
    }
}