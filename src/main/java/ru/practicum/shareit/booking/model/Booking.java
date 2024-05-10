package ru.practicum.shareit.booking.model;

import lombok.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;



@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;// — уникальный идентификатор бронирования;

    @Column(name = "booking_start")
    private LocalDateTime start;// — дата и время начала бронирования;

    @Column(name = "booking_end")
    private LocalDateTime end;// — дата и время конца бронирования;

    @ManyToOne
    @JoinColumn
    private Item item;// — вещь, которую пользователь бронирует;

    @ManyToOne
    private User booker;// — пользователь, который осуществляет бронирование;

    @OneToOne
    private User owner;// — владелец;

    @Enumerated(value = EnumType.STRING)
    private Status status; // — статус бронирования. Может принимать одно из следующих значений:

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return Objects.equals(start, booking.start) && Objects.equals(end, booking.end) && Objects.equals(item, booking.item) && Objects.equals(booker, booking.booker) && status == booking.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end, item, booker, status);
    }
}
