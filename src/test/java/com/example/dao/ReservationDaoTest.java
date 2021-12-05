package com.example.dao;

import com.example.TestConfig;
import com.example.entity.Reservation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

@Transactional
@SpringBootTest(classes = TestConfig.class)
class ReservationDaoTest {

  @Autowired private ReservationDao dao;

  @Autowired private JdbcTemplate jdbcTemplate;

  @BeforeEach
  void setUp() {
    JdbcTestUtils.deleteFromTables(jdbcTemplate, "reservation");
  }

  @AfterEach
  void tearDown() {}

  @Test
  void selectAll() {
    // data setup
    var entity = new Reservation();
    entity.name = "foo";
    dao.insert(entity);
    var actual = dao.selectAll();
    assertThat(actual)
        .hasSize(1)
        .extracting("id", "name")
        .containsExactly(tuple(entity.id, entity.name));
  }

  @Test
  void insert() {
    var entity = new Reservation();
    entity.name = "foo";
    dao.insert(entity);

    var actual = dao.selectById(entity.id);
    assertThat(actual).isPresent();
    assertThat(actual.get()).extracting("id", "name").containsExactly(entity.id, entity.name);
  }

  @Test
  void selectById() {
    var entity = new Reservation();
    entity.name = "foo";
    dao.insert(entity);

    var actual = dao.selectById(entity.id);
    assertThat(actual).isPresent();
    assertThat(actual.get()).extracting("id", "name").containsExactly(entity.id, "foo");
  }
}
