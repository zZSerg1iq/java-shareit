package ru.practicum.shareit.server.user.model;

import lombok.*;
import ru.practicum.shareit.server.item.model.Item;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
    private List<Item> itemList;

}
