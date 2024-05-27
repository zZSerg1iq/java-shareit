package ru.practicum.shareit.server.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.server.item.model.Item;
import ru.practicum.shareit.server.item.repository.ItemRepository;
import ru.practicum.shareit.server.request.model.ItemRequest;
import ru.practicum.shareit.server.request.repository.ItemRequestRepository;
import ru.practicum.shareit.server.user.model.User;
import ru.practicum.shareit.server.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ItemRequestDAOTest {

    @Autowired
    private UserRepository userDAO;
    @Autowired
    private ItemRepository itemDAO;
    @Autowired
    private ItemRequestRepository itemRequestDAO;

    private User user1;
    private User user2;
    private Item item2FromUser1;
    private Item item3FromUser2;
    private ItemRequest itemRequest1WithUser1;
    private ItemRequest itemRequest2WithUser1;
    private ItemRequest itemRequest3WithUser2;
    private final LocalDateTime now = LocalDateTime.now();

    @BeforeEach
    @Transactional
    void init() {
        user1 = User.builder().name("RuRu").email("RuRu@yandex.ru").build();
        user2 = User.builder().name("ComCom").email("ComCom@gmail.com").build();
        userDAO.save(user1);
        userDAO.save(user2);

        Item item1FromUser1 = Item.builder().name("Дрель").description("Простая дрель").owner(user1).build();
        item2FromUser1 = Item.builder().name("Отвертка").description("Аккумуляторная отвертка").owner(user1).build();
        item3FromUser2 = Item.builder().name("Телевизор").description("Телевизор 40 дюймов.").owner(user2).build();
        itemDAO.save(item1FromUser1);
        itemDAO.save(item2FromUser1);
        itemDAO.save(item3FromUser2);

        itemRequest1WithUser1 = ItemRequest.builder().description("Хотел бы воспользоваться отверткой").requester(user1).created(now).build();
        itemRequest2WithUser1 = ItemRequest.builder().description("Нужен диван.").requester(user1).created(now).build();
        itemRequest3WithUser2 = ItemRequest.builder().description("Срочно ищу телевизор.").requester(user2).created(now).build();
        itemRequestDAO.save(itemRequest1WithUser1);
        itemRequestDAO.save(itemRequest2WithUser1);
        itemRequestDAO.save(itemRequest3WithUser2);
    }

    @Test
    @DisplayName("DataJpaTest: поиск запросов на предметы по ID создателя запроса, возвращается корректное количество предметов.")
    public void testFindAllFromOtherUsers_ByRequesterId_ReturnCorrectItemsReturned() {
        List<ItemRequest> itemRequestByUser1 = itemRequestDAO.findAllFromOtherUsers(user2.getId(), 0, 20);
        assertThat(itemRequestByUser1).containsExactly(itemRequest1WithUser1, itemRequest2WithUser1);

        List<ItemRequest> itemsByUser2 = itemRequestDAO.findAllFromOtherUsers(user1.getId(), 0, 20);
        assertThat(itemsByUser2).containsExactly(itemRequest3WithUser2);
    }

    @Test
    @DisplayName("DataJpaTest: поиск запросов на предметы по ID создателя запроса, с использованием параметров пагинации, возвращается корректное количество предметов.")
    public void testFindAllFromOtherUsers_ByRequesterIdAndOffset_ReturnCorrectItemsReturned() {
        List<ItemRequest> itemRequestByUser1 = itemRequestDAO.findAllFromOtherUsers(user2.getId(), 1, 2);
        assertThat(itemRequestByUser1).containsExactly(itemRequest2WithUser1);

        List<ItemRequest> itemsByUser2 = itemRequestDAO.findAllFromOtherUsers(user1.getId(), 1, 2);
        assertThat(itemsByUser2).isEmpty();

    }
}
