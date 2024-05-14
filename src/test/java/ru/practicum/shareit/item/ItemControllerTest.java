package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@ActiveProfiles("test")
@WebMvcTest(controllers = ItemController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ItemControllerTest {

    private static final String USER_HEADER = "X-Sharer-User-Id";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ItemService service;

    private ItemDto inputDTO;
    private final Long itemId1 = 1L;
    private final Long userId1 = 1L;

    @BeforeEach
    void setUp() {

        inputDTO = ItemDto.builder()
                .name("Дрель")
                .description("Простая дрель")
                .available(true)
                .build();
    }

    @Test
    @SneakyThrows
    @DisplayName("WebMvcTest: создать предмет, передается пустое поле name, " +
            "возвращается ответ: HttpStatus.BAD_REQUEST.")
    void testCreateItem_WithEmptyName_ResultException() {

        log.info("Start test: создания предмет, передается пустое поле name.");

        inputDTO.setName(null);

        mvc.perform(post("/items")
                        .header(USER_HEADER, userId1)
                        .content(mapper.writeValueAsString(inputDTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        MethodArgumentNotValidException.class));

        verify(service, never()).createItem(any(ItemDto.class), anyLong());

        log.info("End test: создать предмет, передается пустое поле name, " +
                "возвращается ответ: HttpStatus.BAD_REQUEST.");
    }
}