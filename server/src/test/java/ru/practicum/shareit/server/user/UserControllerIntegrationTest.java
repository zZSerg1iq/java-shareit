package ru.practicum.shareit.server.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.server.exceptions.UserNotFoundException;
import ru.practicum.shareit.server.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    private final ObjectMapper mapper = new ObjectMapper();

    private UserDto userDto;
    private UserDto outputDto;

    private final Long userId1 = 1L;
    private final Long userId2 = 3L;
    private final Long invalidId = 999L;

    public void setUp() {

        userDto = UserDto.builder()
                .email("RuRu@yandex.ru")
                .name("RuRu")
                .build();

        outputDto = UserDto.builder()
                .id(userId1)
                .email("RuRu@yandex.ru")
                .name("RuRu")
                .build();
    }

    @Test
    @Order(0)
    @SneakyThrows
    public void testCreateUser_ResulStatusCreated() {
        setUp();

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(outputDto)));
    }

    @Test
    @Order(2)
    @SneakyThrows
    public void testGetUserById_ResulStatusOk() {
        setUp();

        mvc.perform(get("/users/{userId}", userId1))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(outputDto)))
                .andReturn();
    }

    @Test
    @Order(3)
    @SneakyThrows
    public void testGetUserById_WithInvalidId_ResulStatusNotFound() {
        mvc.perform(get("/users/{invalidUserId}", invalidId))
                .andExpect(status().isNotFound())
                .andExpect(result ->
                        assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                                UserNotFoundException.class));
    }

    @Test
    @Order(4)
    @SneakyThrows
    public void testUpdateUser_OnlyEmail_ResulStatusOk() {
        setUp();
        UserDto userInputDTO = UserDto.builder().email("updateRuRu@yandex.ru").build();
        outputDto.setEmail("updateRuRu@yandex.ru");

        mvc.perform(patch("/users/{userId}", userId1)
                        .content(mapper.writeValueAsString(userInputDTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(outputDto)));
    }

    @Test
    @Order(5)
    @SneakyThrows
    public void testUpdateUser_WithInvalidId_ResulStatusNotFound() {
        setUp();
        userDto.setName("updateRuRu");

        mvc.perform(patch("/users/{userId}", invalidId)
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        UserNotFoundException.class));
    }


    @Test
    @Order(6)
    @SneakyThrows
    public void testDeleteUserResulStatusOk() {
        mvc.perform(delete("/users/{userId}", userId1))
                .andExpect(status().isOk());
    }


    @Test
    @Order(7)
    @SneakyThrows
    public void testDeleteUserStatusOk() {
        mvc.perform(delete("/users/{userId}", userId1))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        UserNotFoundException.class));
    }
}