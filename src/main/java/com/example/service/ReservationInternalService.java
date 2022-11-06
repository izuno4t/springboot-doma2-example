package com.example.service;


import com.example.dao.ReservationDao;
import com.example.entity.Reservation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
class ReservationInternalService {

    private final ReservationDao dao;

    ReservationInternalService(ReservationDao dao) {
        this.dao = dao;
    }

    @Transactional(propagation = Propagation.NESTED)
    public int create(Reservation entity) {
        return dao.insert(entity);
    }

    @Transactional(propagation = Propagation.NESTED)
    public int update(Reservation entity) {
        return dao.update(entity);
    }
}
