package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailableIs(String name, String desc, boolean defaultTrue);

    List<Item> findItemsByOwnerId(Long ownerId);

    List<Item> findAllByOwnerIdOrderById(Long ownerId);

    boolean existsItemByIdAndOwner_Id(Long itemId, Long ownerId);
}
