package com.example.dao;

import com.example.TestConfig;
import com.example.entity.Reservation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

@SpringBootTest(classes = TestConfig.class)
@Transactional
class ReservationDaoTest {

    @Autowired
    private ReservationDao dao;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void selectAll() {
        // data setup
        {
            var entity = new Reservation();
            entity.name = "foo";
            dao.insert(entity);
        }

        var actual = dao.selectAll();
        assertThat(actual).hasSize(1).extracting("id", "name").containsExactly(tuple(1, "foo"));

    }

    @Test
    void insert() {
    }
}