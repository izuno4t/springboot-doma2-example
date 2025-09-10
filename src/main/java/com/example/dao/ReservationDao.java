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

@ConfigAutowireable
@Dao
public interface ReservationDao {

    @Select
    Optional<Reservation> selectById(ReservationId id);

    @Select
    List<Reservation> selectAll();

    @Insert
    int insert(Reservation reservation);

    @Update
    int update(Reservation reservation);
}
