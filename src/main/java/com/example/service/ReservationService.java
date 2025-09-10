package com.example.service;

import com.example.dao.ReservationDao;
import com.example.entity.Reservation;
import com.example.entity.ReservationId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ReservationService {

    private static final Logger logger = LoggerFactory.getLogger(ReservationService.class);

    private final ReservationDao dao;
    private final ReservationInternalService internalService;

    public ReservationService(ReservationDao dao, ReservationInternalService internalService) {
        this.dao = dao;
        this.internalService = internalService;
    }

    public Optional<Reservation> findById(ReservationId id) {
        return dao.selectById(id);
    }

    @Transactional
    public int create(Reservation entity) {
        return dao.insert(entity);
    }

    public int save(Reservation entity) {
        if (entity.getId() == null) {
            return internalService.create(entity);
        } else {
            return internalService.update(entity);
        }
    }


}
