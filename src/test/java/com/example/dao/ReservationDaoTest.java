package com.example.dao;

import com.example.TestConfig;
import com.example.entity.Reservation;
import com.example.entity.ReservationId;
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

@SpringBootTest(classes = TestConfig.class)
@Transactional
class ReservationDaoTest {

    @Autowired
    private ReservationDao dao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "reservation");
    }

    @AfterEach
    void tearDown() {
    }

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
                .containsExactly(tuple(entity.getId(), entity.name));
    }

    @Test
    void insert() {
        var entity = new Reservation();
        entity.name = "foo";
        dao.insert(entity);

        var actual = dao.selectById(entity.getId());
        assertThat(actual).isPresent();
        assertThat(actual.get()).extracting("id", "name").containsExactly(entity.getId(), entity.name);
    }

    @Test
    void selectById() {
        var entity = new Reservation();
        entity.name = "foo";
        dao.insert(entity);

        var actual = dao.selectById(entity.getId());
        assertThat(actual).isPresent();
        assertThat(actual.get()).extracting("id", "name").containsExactly(entity.getId(), "foo");
    }

    @Test
    void selectById_NotFound() {
        // Try to select an ID that doesn't exist
        var actual = dao.selectById(ReservationId.of(999));
        assertThat(actual).isEmpty();
    }

    @Test
    void selectById_NullId() {
        // Test with null ID
        var actual = dao.selectById(null);
        assertThat(actual).isEmpty();
    }

    @Test
    void selectAll_EmptyTable() {
        // Test selectAll when table is empty (setUp() already clears table)
        var actual = dao.selectAll();
        assertThat(actual).isEmpty();
    }

    @Test
    void selectAll_MultipleRecords() {
        // Insert multiple records
        var entity1 = new Reservation();
        entity1.name = "Alice";
        dao.insert(entity1);

        var entity2 = new Reservation();
        entity2.name = "Bob";
        dao.insert(entity2);

        var entity3 = new Reservation();
        entity3.name = "Charlie";
        dao.insert(entity3);

        var actual = dao.selectAll();
        assertThat(actual)
                .hasSize(3)
                // Results should be ordered by name according to SQL query
                .extracting("name")
                .containsExactlyInAnyOrder("Alice", "Bob", "Charlie");
    }

    @Test
    void insert_VerifyReturnValue() {
        var entity = new Reservation();
        entity.name = "test";

        int result = dao.insert(entity);

        // Should return number of affected rows (1)
        assertThat(result).isEqualTo(1);
        assertThat(entity.getId()).isNotNull(); // ID should be auto-generated
    }

    @Test
    void insert_WithNullName() {
        var entity = new Reservation();
        entity.name = null; // name can be null according to schema

        int result = dao.insert(entity);

        assertThat(result).isEqualTo(1);
        assertThat(entity.getId()).isNotNull();

        var inserted = dao.selectById(entity.getId());
        assertThat(inserted).isPresent();
        assertThat(inserted.get().name).isNull();
    }

    @Test
    void update() {
        // First insert a record
        var entity = new Reservation();
        entity.name = "original";
        dao.insert(entity);

        // Then update it
        entity.name = "updated";
        int result = dao.update(entity);

        // Verify update was successful
        assertThat(result).isEqualTo(1);

        var updated = dao.selectById(entity.getId());
        assertThat(updated).isPresent();
        assertThat(updated.get().name).isEqualTo("updated");
    }

    @Test
    void update_NonExistentRecord() {
        // Try to update a record that doesn't exist
        var entity = new Reservation();
        entity.setId(ReservationId.of(999)); // Non-existent ID
        entity.name = "test";

        int result = dao.update(entity);

        // Should return 0 (no rows affected)
        assertThat(result).isEqualTo(0);
    }

    @Test
    void update_WithNullName() {
        // First insert a record
        var entity = new Reservation();
        entity.name = "original";
        dao.insert(entity);

        // Update to null name
        entity.name = null;
        int result = dao.update(entity);

        assertThat(result).isEqualTo(1);

        var updated = dao.selectById(entity.getId());
        assertThat(updated).isPresent();
        assertThat(updated.get().name).isNull();
    }
}
