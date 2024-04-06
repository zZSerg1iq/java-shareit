package ru.practicum.shareit.booking.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.util.Date;

/**
 * TODO Sprint add-bookings.
 */
@NoArgsConstructor
@Data
@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;// — уникальный идентификатор бронирования;

    @Column(name = "booking_start")
    private Date start;// — дата и время начала бронирования;

    @Column(name = "booking_end")
    private Date end;// — дата и время конца бронирования;

    @ManyToOne
    @JoinColumn
    private Item item;// — вещь, которую пользователь бронирует;

    @OneToOne
    private User booker;// — пользователь, который осуществляет бронирование;

    private Status status; // — статус бронирования. Может принимать одно из следующих значений:
    // WAITING — новое бронирование, ожидает одобрения, APPROVED — бронирование подтверждено владельцем, REJECTED — бронирование    отклонено владельцем, CANCELED — бронирование отменено создателем.
}
