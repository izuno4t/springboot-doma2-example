package com.example.dao;

import com.example.entity.Reservation;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.List;
import java.util.Optional;

@ConfigAutowireable
@Dao
public interface ReservationDao {

  @Select
  Optional<Reservation> selectById(Integer id);

  @Select
  List<Reservation> selectAll();

  @Insert
  int insert(Reservation reservation);
}
