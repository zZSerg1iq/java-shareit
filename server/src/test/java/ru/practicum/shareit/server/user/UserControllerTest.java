package ru.practicum.shareit.server.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.server.exceptions.NotFoundException;
import ru.practicum.shareit.server.user.dto.UserDto;
import ru.practicum.shareit.server.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Slf4j
@ActiveProfiles("test")
@WebMvcTest(controllers = UserController.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private UserService service;

    private UserDto userInputDTO;
    private UserDto userOutputDTO;
    private final Long userId = 1L;
    private final NotFoundException notFoundException = new NotFoundException("Exception");

    @BeforeEach
    void setUp() {

        userInputDTO = UserDto.builder()
                .id(null)
                .email("ruru@yandex.ru")
                .name("RuRu")
                .build();
        userOutputDTO = UserDto.builder()
                .id(userId)
                .email("ruru@yandex.ru")
                .name("RuRu")
                .build();
    }

    @Test
    @SneakyThrows
    @DisplayName("WebMvcTest: создать пользователя, возвращается ответ: HttpStatus.CREATED.")
    void testCreateUser_ResultStatusCreated() {

        log.info("Start test: создать пользователя.");

        when(service.addUser(any(UserDto.class))).thenReturn(userOutputDTO);

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userInputDTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(userOutputDTO)));

        verify(service, times(1)).addUser(any(UserDto.class));

        log.info("End test: создать пользователя, возвращается ответ: HttpStatus.CREATED.");
    }

    @Test
    @SneakyThrows
    @DisplayName("WebMvcTest: получить пользователя по ID, возвращается ответ: HttpStatus.OK.")
    void testGetUser_ById_ResultStatusOk() {

        log.info("Start test: получить пользователя по ID.");

        when(service.getUserById(anyLong())).thenReturn(userOutputDTO);

        mvc.perform(get("/users/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(userOutputDTO)));

        verify(service, times(1)).getUserById(anyLong());

        log.info("End test: получить пользователя по ID, возвращается ответ: HttpStatus.OK.");
    }

    @Test
    @SneakyThrows
    @DisplayName("WebMvcTest: получить пользователя по неверному ID, возвращается ответ: HttpStatus.NOT_FOUND.")
    void testGetUser_ByInvalidId_ResultStatusNotFound() {

        log.info("Start test: получить пользователя по неверному ID.");

        when(service.getUserById(anyLong())).thenThrow(notFoundException);

        Long invalidId = 999L;
        mvc.perform(get("/users/{userId}", invalidId))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        NotFoundException.class));


        verify(service, times(1)).getUserById(invalidId);

        log.info("End test: получить пользователя по неверному ID, возвращается ответ: HttpStatus.NOT_FOUND.");
    }

    @Test
    @SneakyThrows
    @DisplayName("WebMvcTest: получить всех пользователей, возвращается ответ: HttpStatus.OK.")
    void testGetAllUsers_ResultStatusOk() {

        log.info("Start test: получить всех пользователей.");
        UserDto userOutputDTO1 = UserDto.builder()
                .id(1L)
                .email("updateRuRu@yandex.ru")
                .name("RuRu")
                .build();

        UserDto userOutputDTO2 = UserDto.builder()
                .id(3L)
                .email("RuRu@yandex.ru")
                .name("updateRuRu")
                .build();

        List<UserDto> userList = Arrays.asList(userOutputDTO1, userOutputDTO2);

        when(service.getAll()).thenReturn(userList);

        mvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(userList)));

        verify(service, times(1)).getAll();

        log.info("End test: получить всех пользователей, возвращается ответ: HttpStatus.OK.");
    }

    @Test
    @SneakyThrows
    @DisplayName("WebMvcTest: удалить пользователя по ID, возвращается ответ: HttpStatus.NO_CONTENT.")
    void testDeleteUser_ResultStatusNoContent() {

        log.info("Start test: удалить пользователя по ID.");

        mvc.perform(delete("/users/{userId}", userId))
                .andExpect(status().isOk());

        verify(service, times(1)).deleteUser(userId);

        log.info("End test: удалить пользователя по ID, возвращается ответ: HttpStatus.NO_CONTENT.");
    }
}