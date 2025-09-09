package com.example.service;

import com.example.TestConfig;
import com.example.entity.Reservation;
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

            var actual = service.findById(entity.id).orElseThrow();
            assertThat(actual).extracting("id", "name").containsExactly(entity.id, "foo");
        }

        @Test
        void データがあるのでUpdateで更新される() {

            var entity = new Reservation();
            entity.name = "foo";
            service.save(entity);

            entity.name = "bar";
            service.save(entity);

            var actual = service.findById(entity.id).orElseThrow();
            assertThat(actual).extracting("id", "name").containsExactly(entity.id, "bar");

        }

    }

}