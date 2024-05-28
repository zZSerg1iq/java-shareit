package ru.practicum.shareit.server.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.server.item.model.Comment;

import java.util.List;


public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByItemIdOrderByCreatedDesc(Long itemId);

    List<Comment> findAllByItemIdInOrderByCreatedDesc(List<Long> itemId);
}
