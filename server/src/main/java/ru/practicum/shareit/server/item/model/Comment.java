package ru.practicum.shareit.server.item.model;

import lombok.*;
import ru.practicum.shareit.server.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
