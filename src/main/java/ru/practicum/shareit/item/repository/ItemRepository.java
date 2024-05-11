package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailableIs(String name, String desc, boolean defaultTrue);

    @Query("SELECT i FROM Item i WHERE (LOWER(i.name) LIKE LOWER(:name) OR LOWER(i.description) LIKE LOWER(:desc)) AND i.available = :available")
    List<Item> findItemsByNameOrDescriptionAndAvailability(String name, String desc, boolean available);


    List<Item> findAllByOwnerIdOrderById(Long ownerId);

    boolean existsItemByIdAndOwner_Id(Long itemId, Long ownerId);

    List<Item> findAllByRequestId(Long requestId);

    List<Item> findAllByRequest_IdIn(List<Long> requestIds);
}
