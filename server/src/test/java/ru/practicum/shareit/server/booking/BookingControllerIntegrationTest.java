package ru.practicum.shareit.server.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.server.booking.dto.BookingDto;
import ru.practicum.shareit.server.booking.model.Status;
import ru.practicum.shareit.server.exceptions.*;
import ru.practicum.shareit.server.item.dto.ItemDto;
import ru.practicum.shareit.server.item.model.Item;
import ru.practicum.shareit.server.item.repository.ItemRepository;
import ru.practicum.shareit.server.user.dto.UserDto;
import ru.practicum.shareit.server.user.model.User;
import ru.practicum.shareit.server.user.repository.UserRepository;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class BookingControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userDAO;

    @Autowired
    private ItemRepository itemDAO;
    private final ObjectMapper mapper = new ObjectMapper();

    private User user1;
    private User user2;

    private Item item1;
    private Item item2;
    private Item item3;

    private BookingDto bookingInputDTO;
    private BookingDto bookingOutputDTO;

    private static final String USER_HEADER = "X-Sharer-User-Id";

    private final Long userId1 = 1L;
    private final Long userId2 = 2L;
    private final Long itemId1 = 1L;
    private final Long bookingId1 = 1L;
    private final Long invalidId = 999L;
    private final int from = 0;
    private final int size = 2;
    private final LocalDateTime now = LocalDateTime.now();
    private final LocalDateTime startTime = now.plusHours(1);
    private final LocalDateTime endTime = now.plusHours(2);


    public void init() {
        userDAO.save(user1);
        userDAO.save(user2);
        itemDAO.save(item1);
        itemDAO.save(item2);
        itemDAO.save(item3);
    }

    @BeforeEach
    void setUp() {
        mapper.registerModule(new JavaTimeModule());
        user1 = User.builder()
                .email("ruru@yandex.ru")
                .name("RuRu")
                .build();
        UserDto userOutputDTO1 = UserDto.builder()
                .email("ruru@yandex.ru")
                .name("RuRu")
                .build();

        user2 = User.builder()
                .email("comcom@gmail.com")
                .name("ComCom")
                .build();

        item1 = Item.builder()
                .name("Дрель")
                .description("Простая дрель")
                .available(true)
                .owner(user1)
                .build();

        item2 = Item.builder()
                .name("Отвертка")
                .description("Крестовая отвертка")
                .available(true)
                .owner(user2)
                .build();
        Long itemId2 = 2L;
        ItemDto itemShortOutputDTO2 = ItemDto.builder()
                .id(itemId2)
                .name("Отвертка")
                .description("Крестовая отвертка")
                .available(true)
                .build();

        item3 = Item.builder()
                .name("Стул")
                .description("Пластиковый стул")
                .available(false)
                .owner(user1)
                .build();

        bookingInputDTO = BookingDto.builder()
                .start(startTime)
                .end(endTime)
                .itemId(itemId2)
                .build();

        bookingOutputDTO = BookingDto.builder()
                .id(bookingId1)
                .start(startTime)
                .end(endTime)
                .status(Status.WAITING)
                .booker(userOutputDTO1)
                .item(itemShortOutputDTO2)
                .build();
    }

    @Test
    @Order(0)
    @SneakyThrows
    public void testCreateBooking() {
        init();

        mvc.perform(post("/bookings")
                        .header(USER_HEADER, userId1)
                        .content(mapper.writeValueAsString(bookingInputDTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", is(bookingOutputDTO.getId()), Long.class));


    }

    @Test
    @Order(1)
    @SneakyThrows
    public void testGetBookingById() {
        mvc.perform(get("/bookings/{bookingId}", bookingId1)
                        .header(USER_HEADER, userId1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", is(bookingOutputDTO.getId()), Long.class))
                .andReturn();
    }

    @Test
    @Order(2)
    @SneakyThrows
    public void testUpdateBooking() {
        mvc.perform(patch("/bookings/{bookingId}", bookingId1)
                        .header(USER_HEADER, invalidId)
                        .param("approved", "true")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        NotFoundException.class));
    }

    @Test
    @Order(3)
    @SneakyThrows
    public void testUpdateBooking2() {
        mvc.perform(patch("/bookings/{bookingId}", invalidId)
                        .header(USER_HEADER, userId1)
                        .param("approved", "true")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        NotFoundException.class));
    }

    @Test
    @Order(4)
    @SneakyThrows
    public void testUpdateBooking3() {


        mvc.perform(patch("/bookings/{bookingId}", bookingId1)
                        .header(USER_HEADER, userId1)
                        .param("approved", "true")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        NotFoundException.class));


    }


    @Test
    @Order(5)
    @SneakyThrows
    public void testUpdateBooking4() {
        mvc.perform(patch("/bookings/{bookingId}", bookingId1)
                        .header(USER_HEADER, userId2)
                        .param("approved", "false")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(6)
    @SneakyThrows
    public void testGetAllBookings() {
        bookingOutputDTO = BookingDto.builder().build();
        bookingOutputDTO.setId(bookingId1);

        List<BookingDto> bookings = Collections.singletonList(bookingOutputDTO);

        mvc.perform(get("/bookings")
                        .header(USER_HEADER, userId1)
                        .param("state", "ALL")
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(7)
    @SneakyThrows
    public void testGetAllBookings2() {
        mvc.perform(get("/bookings")
                        .header(USER_HEADER, userId1)
                        .param("state", "CURRENT")
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of())));
    }

    @Test
    @Order(8)
    @SneakyThrows
    public void testGetAllBookings3() {
        mvc.perform(get("/bookings")
                        .header(USER_HEADER, userId1)
                        .param("state", "PAST")
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of())));
    }


    @Test
    @Order(9)
    @SneakyThrows
    public void testGetAllBookings4() {
        mvc.perform(get("/bookings")
                        .header(USER_HEADER, userId1)
                        .param("state", "WAITING")
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of())));
    }


    @Test
    @Order(10)
    @SneakyThrows
    public void testGetBookingById2() {
        mvc.perform(get("/bookings/{bookingId}", invalidId)
                        .header(USER_HEADER, userId1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        NotFoundException.class));
    }


    @Test
    @Order(11)
    @SneakyThrows
    public void testGetAllBookings5() {
        mvc.perform(get("/bookings/owner")
                        .header(USER_HEADER, userId2)
                        .param("state", "CURRENT")
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of())));
    }

    @Test
    @Order(12)
    @SneakyThrows
    public void testGetAllBookings6() {
        mvc.perform(get("/bookings/owner")
                        .header(USER_HEADER, userId2)
                        .param("state", "PAST")
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of())));
    }


    @Test
    @Order(13)
    @SneakyThrows
    public void testGetAllBookings7() {
        mvc.perform(get("/bookings/owner")
                        .header(USER_HEADER, userId2)
                        .param("state", "WAITING")
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of())));
    }

    @Test
    @Order(14)
    @SneakyThrows
    public void testGetAllBookings8() {
        mvc.perform(get("/bookings/owner")
                        .header(USER_HEADER, userId2)
                        .param("state", "REJECTED")
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Order(15)
    @SneakyThrows
    public void testGetAllBookings9() {
        mvc.perform(get("/bookings/owner")
                .header(USER_HEADER, userId2)
                .param("state", "Unknown")
                .param("from", String.valueOf(from))
                .param("size", String.valueOf(size))
                .accept(MediaType.APPLICATION_JSON));
    }

    @Test
    @Order(16)
    @SneakyThrows
    public void testGetAllBookings10() {
        mvc.perform(get("/bookings/owner")
                        .header(USER_HEADER, invalidId)
                        .param("state", "ALL")
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        UserNotFoundException.class));
    }

    @Test
    @Order(17)
    @SneakyThrows
    public void testCreateBooking_WithUserItemBooker() {
        bookingInputDTO = BookingDto.builder()
                .start(startTime)
                .end(endTime)
                .itemId(itemId1)
                .build();

        mvc.perform(post("/bookings")
                        .header(USER_HEADER, userId1)
                        .content(mapper.writeValueAsString(bookingInputDTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        NotFoundException.class));
    }

    @Test
    @Order(18)
    @SneakyThrows
    public void testCreateBooking_WithItemIdInvalid() {
        bookingInputDTO = BookingDto.builder()
                .start(startTime)
                .end(endTime)
                .itemId(invalidId)
                .build();

        mvc.perform(post("/bookings")
                        .header(USER_HEADER, userId2)
                        .content(mapper.writeValueAsString(bookingInputDTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        ItemNotFoundException.class));
    }

    @Test
    @Order(19)
    @SneakyThrows
    public void testCreateBooking_WithStartAfterEndTime() {
        bookingInputDTO = BookingDto.builder()
                .start(LocalDateTime.now().plusDays(2))
                .end(LocalDateTime.now().plusDays(1))
                .itemId(itemId1)
                .build();

        mvc.perform(post("/bookings")
                        .header(USER_HEADER, userId2)
                        .content(mapper.writeValueAsString(bookingInputDTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        ValidateException.class));
    }

    @Test
    @Order(20)
    @SneakyThrows
    public void testCreateBooking_WithStartEqualsEndTime() {
        LocalDateTime startAndEndTime = LocalDateTime.now().plusDays(1);
        bookingInputDTO = BookingDto.builder()
                .start(startAndEndTime)
                .end(startAndEndTime)
                .itemId(itemId1)
                .build();

        mvc.perform(post("/bookings")
                        .header(USER_HEADER, userId2)
                        .content(mapper.writeValueAsString(bookingInputDTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        ValidateException.class));
    }

    @Test
    @Order(21)
    @SneakyThrows
    public void testCreateBooking_WithItemStatusNotAvailable() {
        Long itemId3 = 3L;
        bookingInputDTO = BookingDto.builder()
                .start(startTime)
                .end(endTime)
                .itemId(itemId3)
                .build();

        mvc.perform(post("/bookings")
                        .header(USER_HEADER, userId2)
                        .content(mapper.writeValueAsString(bookingInputDTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        AvailableException.class));

    }
}
