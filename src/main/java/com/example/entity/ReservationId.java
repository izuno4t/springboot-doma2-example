package com.example.entity;

import org.seasar.doma.Domain;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Reservation ID value object.
 * <p>
 * This class represents a reservation identifier as a value object,
 * wrapping the underlying Integer ID with proper value semantics.
 * </p>
 */
@Domain(valueType = Integer.class, accessorMethod = "value", factoryMethod = "of")
public record ReservationId(Integer value) {

    /**
     * Creates a new ReservationId with the specified value.
     *
     * @param value the ID value, may be null for new entities
     */
    public ReservationId {
    }

    /**
     * Gets the underlying ID value.
     *
     * @return the ID value, may be null for new entities
     */
    @Override
    public Integer value() {
        return value;
    }

    /**
     * Creates a ReservationId from an Integer value.
     *
     * @param value the ID value, may be null
     * @return a new ReservationId instance, or null if value is null
     */
    public static ReservationId of(Integer value) {
        return value != null ? new ReservationId(value) : null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReservationId that = (ReservationId) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public @NotNull String toString() {
        return "ReservationId{" +
                "value=" + value +
                '}';
    }
}