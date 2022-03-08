package com.example.service;

import com.example.dao.ReservationDao;
import com.example.entity.Reservation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
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

    public Optional<Reservation> findById(Integer id) {
        return dao.selectById(id);
    }

    @Transactional
    public int create(Reservation entity) {
        return dao.insert(entity);
    }

    public int save(Reservation entity) {
        int affectedRow;
        try {
            affectedRow = internalService.create(entity);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            affectedRow = internalService.update(entity);
        }
        return affectedRow;
    }

    @Service
    static class ReservationInternalService {

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

}
