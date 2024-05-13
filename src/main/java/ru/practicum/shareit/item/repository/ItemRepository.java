package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;


public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query(nativeQuery = true,
            value = "SELECT * FROM item AS i " +
                    "WHERE available = TRUE " +
                    "and " +
                    "(LOWER(name) LIKE CONCAT('%', LOWER(:name), '%') " +
                    "OR " +
                    "LOWER(description) LIKE CONCAT('%', LOWER(:name), '%'))" +
                    "ORDER BY i.id LIMIT :size OFFSET :from")
    List<Item> findByNameOrDescription(String name, Integer size, Integer from);

    @Query(nativeQuery = true,
            value = "SELECT * FROM item AS i " +
                    "LEFT JOIN users u on u.id = i.owner " +
                    "LEFT JOIN public.item_request r on r.id = i.request_id " +
                    "WHERE i.owner = :ownerId " +
                    "ORDER BY i.id LIMIT :size OFFSET :from")
    List<Item> findAllByOwnerIdOrderById(Long ownerId, Integer from, Integer size);

    boolean existsItemByIdAndOwner_Id(Long itemId, Long ownerId);

    List<Item> findAllByRequestId(Long requestId);

    List<Item> findAllByRequest_IdIn(List<Long> requestIds);
}
