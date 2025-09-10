package com.example.service;

import com.example.TestConfig;
import com.example.entity.Reservation;
import com.example.entity.ReservationId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = TestConfig.class)
@Transactional
class ReservationServiceTest {

    @Autowired
    private ReservationService service;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "reservation");
    }

    @AfterEach
    void tearDown() {
    }

    @Nested
    class Save {

        @Test
        void データがないのでinsertされる() {
            var entity = new Reservation();
            entity.name = "foo";
            service.save(entity);

            var actual = service.findById(entity.getId()).orElseThrow();
            assertThat(actual).extracting("id", "name").containsExactly(entity.getId(), "foo");
        }

        @Test
        void データがあるのでUpdateで更新される() {

            var entity = new Reservation();
            entity.name = "foo";
            service.save(entity);

            entity.name = "bar";
            service.save(entity);

            var actual = service.findById(entity.getId()).orElseThrow();
            assertThat(actual).extracting("id", "name").containsExactly(entity.getId(), "bar");

        }

    }

    @Nested
    class Create {

        @Test
        void 新しいデータが作成される() {
            var entity = new Reservation();
            entity.name = "test-create";

            int result = service.create(entity);

            assertThat(result).isEqualTo(1);
            // ID is auto-generated, so entity.id should be populated after creation
            assertThat(entity.getId()).isNotNull();
            
            var actual = service.findById(entity.getId()).orElseThrow();
            assertThat(actual).extracting("id", "name").containsExactly(entity.getId(), "test-create");
        }

    }

    @Nested
    class FindById {

        @Test
        void 存在するIDで検索すると結果が返される() {
            // Create a test entity using save method
            var entity = new Reservation();
            entity.name = "find-test";
            service.save(entity);

            var actual = service.findById(entity.getId());

            assertThat(actual).isPresent();
            assertThat(actual.get()).extracting("id", "name").containsExactly(entity.getId(), "find-test");
        }

        @Test
        void 存在しないIDで検索すると空のOptionalが返される() {
            var actual = service.findById(ReservationId.of(999));

            assertThat(actual).isEmpty();
        }

        @Test
        void nullのIDで検索すると空のOptionalが返される() {
            var actual = service.findById(null);

            assertThat(actual).isEmpty();
        }

    }

}