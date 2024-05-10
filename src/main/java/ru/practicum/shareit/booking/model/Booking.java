package ru.practicum.shareit.booking.model;

import lombok.*;
import org.hibernate.Hibernate;
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
    private Long id;

    @Column(name = "booking_start")
    private LocalDateTime start;

    @Column(name = "booking_end")
    private LocalDateTime end;

    @ManyToOne
    @JoinColumn
    private Item item;

    @ManyToOne
    private User booker;

    @OneToOne
    private User owner;

    @Enumerated(value = EnumType.STRING)
    private Status status;

}
