package com.example.entity;

import org.seasar.doma.Entity;
import org.seasar.doma.GeneratedValue;
import org.seasar.doma.GenerationType;
import org.seasar.doma.Id;

/**
 * Reservation entity representing a reservation record.
 * <p>
 * This entity maps to the reservation table in the database.
 * The ID field uses a value object pattern for type safety.
 * </p>
 */
@Entity
public class Reservation {

    /**
     * The reservation ID as a value object.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private ReservationId id;

    /**
     * The reservation name.
     */
    public String name;

    /**
     * Gets the reservation ID.
     *
     * @return the reservation ID
     */
    public ReservationId getId() {
        return id;
    }

    /**
     * Sets the reservation ID.
     *
     * @param id the reservation ID
     */
    public void setId(ReservationId id) {
        this.id = id;
    }
}
