package ru.practicum.shareit.server.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.server.exceptions.ItemNotFoundException;
import ru.practicum.shareit.server.exceptions.NotFoundException;
import ru.practicum.shareit.server.exceptions.UserNotFoundException;
import ru.practicum.shareit.server.exceptions.ValidateException;
import ru.practicum.shareit.server.item.dto.CommentDto;
import ru.practicum.shareit.server.item.dto.ItemDto;
import ru.practicum.shareit.server.request.model.ItemRequest;
import ru.practicum.shareit.server.request.repository.ItemRequestRepository;
import ru.practicum.shareit.server.user.model.User;
import ru.practicum.shareit.server.user.repository.UserRepository;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class ItemControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userDAO;

    @Autowired
    private ItemRequestRepository itemRequestDAO;

    private static final String USER_HEADER = "X-Sharer-User-Id";
    private ItemDto inputDTO;
    private User user2;
    private final ObjectMapper mapper = new ObjectMapper();
    private final Long itemId1 = 1L;
    private final Long userId1 = 1L;
    private final Long userId2 = 2L;
    private final Long invalidId = 999L;

    public void init() {
        User user1 = User.builder()
                .email("ruru@yandex.ru")
                .name("RuRu")
                .build();
        user2 = User.builder()
                .email("comcom@gmail.com")
                .name("ComCom")
                .build();
        userDAO.save(user1);
        userDAO.save(user2);
    }

    @BeforeEach
    public void setUp() {

        inputDTO = ItemDto.builder()
                .name("Дрель")
                .description("Простая дрель")
                .available(true)
                .build();
    }

    @Test
    @Order(0)
    @SneakyThrows
    public void test1() {
        init();

        mvc.perform(post("/items")
                        .header(USER_HEADER, userId1)
                        .content(mapper.writeValueAsString(inputDTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(1)
    @SneakyThrows
    public void test2CreateItem() {
        mvc.perform(post("/items")
                        .header(USER_HEADER, invalidId)
                        .content(mapper.writeValueAsString(inputDTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        UserNotFoundException.class));
    }

    @Test
    @Order(2)
    @SneakyThrows
    public void test3UpdateItem() {
        inputDTO.setName("Дрель--");

        mvc.perform(patch("/items/{itemId}", invalidId)
                        .header(USER_HEADER, userId1)
                        .content(mapper.writeValueAsString(inputDTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        ItemNotFoundException.class));
    }

    @Test
    @Order(3)
    @SneakyThrows
    public void test4CreateItem2() {
        inputDTO = ItemDto.builder()
                .name("Отвертка")
                .description("Аккумуляторная отвертка")
                .available(true)
                .build();

        mvc.perform(post("/items")
                        .header(USER_HEADER, userId2)
                        .content(mapper.writeValueAsString(inputDTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(4)
    @SneakyThrows
    public void testCreateItem3() {
        inputDTO = ItemDto.builder()
                .name("Телевизор")
                .description("Телевизор 40 дюймов.")
                .available(true)
                .build();

        mvc.perform(post("/items")
                        .header(USER_HEADER, userId2)
                        .content(mapper.writeValueAsString(inputDTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @Test
    @Order(5)
    @SneakyThrows
    public void testGetItem() {
        mvc.perform(get("/items/{itemId}", invalidId)
                        .header(USER_HEADER, userId1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        NotFoundException.class));
    }

    @Test
    @Order(6)
    @SneakyThrows
    public void testGetItem2() {
        mvc.perform(get("/items/{itemId}", itemId1)
                        .header(USER_HEADER, invalidId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @Test
    @Order(7)
    @SneakyThrows
    public void testUpdateItem2() {
        ItemDto itemInputDTO = ItemDto.builder()
                .id(itemId1)
                .requestId(invalidId)
                .build();

        mvc.perform(patch("/items/{itemId}", itemId1)
                        .header(USER_HEADER, userId1)
                        .content(mapper.writeValueAsString(itemInputDTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(8)
    @SneakyThrows
    public void testAddComment() {
        CommentDto commentInputDTO = CommentDto.builder()
                .text("Add comment from user1")
                .build();

        mvc.perform(post("/items/{itemId}/comment", 2L)
                        .header(USER_HEADER, userId2)
                        .content(mapper.writeValueAsString(commentInputDTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        ValidateException.class));
    }

    @Test
    @Order(9)
    @SneakyThrows
    public void testCreateWithRequest() {
        ItemRequest itemRequest = ItemRequest.builder().description("Нужен диван").requester(user2).build();
        itemRequestDAO.save(itemRequest);
        ItemDto itemInputDTO = ItemDto.builder()
                .name("Диван")
                .description("Мягкий диван.")
                .available(true)
                .requestId(1L)
                .build();
        ItemDto itemOutputDTO = ItemDto.builder()
                .id(4L)
                .name("Диван")
                .description("Мягкий диван.")
                .available(true)
                .requestId(1L)
                .build();

        mvc.perform(post("/items")
                        .header(USER_HEADER, userId1)
                        .content(mapper.writeValueAsString(itemInputDTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", is(itemOutputDTO.getId()), Long.class))
                .andExpect(jsonPath("name", is(itemOutputDTO.getName())))
                .andExpect(jsonPath("description", is(itemOutputDTO.getDescription())))
                .andExpect(jsonPath("available", is(itemOutputDTO.getAvailable())));
    }

    @Test
    @Order(10)
    @SneakyThrows
    public void testUpdateItem3() {

        mvc.perform(patch("/items/{itemId}", itemId1)
                        .header(USER_HEADER, userId2)
                        .content(mapper.writeValueAsString(inputDTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        UserNotFoundException.class));
    }

    @Test
    @Order(11)
    @SneakyThrows
    public void testAddComment2() {
        CommentDto commentInputDTO = CommentDto.builder()
                .text("Add comment from user1")
                .build();

        mvc.perform(post("/items/{itemId}/comment", invalidId)
                        .header(USER_HEADER, userId2)
                        .content(mapper.writeValueAsString(commentInputDTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        ItemNotFoundException.class));
    }

    @Test
    @Order(12)
    @SneakyThrows
    public void testGetAllNoTextValidation() {
        String text = "";

        int from = 0;
        int size = 2;
        mvc.perform(
                        get("/items/search")
                                .header(USER_HEADER, userId2)
                                .param("text", text)
                                .param("from", String.valueOf(from))
                                .param("size", String.valueOf(size))
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }
}


