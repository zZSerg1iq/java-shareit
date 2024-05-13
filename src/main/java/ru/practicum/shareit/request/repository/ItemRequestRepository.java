package ru.practicum.shareit.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    List<ItemRequest> findAllByRequester_Id(Long requesterId);

    @Query(nativeQuery = true,
            value = "SELECT * FROM item_request AS r " +
                    "LEFT JOIN users u on u.id = r.requester_id " +
                    "WHERE NOT r.requester_id = :userId ORDER BY r.id LIMIT :size OFFSET :from")
    List<ItemRequest> findAllFromOtherUsers(Long userId, Integer from, Integer size);


}
