package ru.practicum.shareit.request.model;

import lombok.*;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.util.Date;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;  // текст запроса, содержащий описание требуемой вещи;

    @ManyToOne
    @JoinColumn
    private User requestor;  // пользователь, создавший запрос;

    private Date created;  // дата и время создания запроса.

}
