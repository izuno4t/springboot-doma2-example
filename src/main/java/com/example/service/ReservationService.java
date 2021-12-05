package com.example.service;

import com.example.dao.ReservationDao;
import com.example.entity.Reservation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReservationService {

  private final ReservationDao reservationDao;

  public ReservationService(ReservationDao reservationDao) {
    this.reservationDao = reservationDao;
  }

  @Transactional
  public int create(Reservation entity) {
    return reservationDao.insert(entity);
  }
}
