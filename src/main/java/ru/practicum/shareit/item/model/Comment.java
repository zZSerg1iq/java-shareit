package ru.practicum.shareit.item.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // — уникальный идентификатор комментария;

    private String text; // — содержимое комментария;

    @ManyToOne
    private Item item; // — вещь, к которой относится комментарий;

    @OneToOne
    private User author; // — автор комментария;

    private LocalDateTime created; // — дата создания комментария.

}
