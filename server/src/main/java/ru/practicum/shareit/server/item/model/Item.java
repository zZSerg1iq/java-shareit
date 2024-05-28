package ru.practicum.shareit.server.item.model;

import lombok.*;
import ru.practicum.shareit.server.request.model.ItemRequest;
import ru.practicum.shareit.server.user.model.User;

import javax.persistence.*;


@Entity
@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private boolean available;

    @ManyToOne
    @JoinColumn(name = "owner")
    private User owner;

    @OneToOne
    @JoinColumn(name = "renter")
    private User renter;

    @OneToOne
    @JoinColumn
    private ItemRequest request;

}
