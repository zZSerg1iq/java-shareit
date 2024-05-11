package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.RequestService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ActiveProfiles("test")
@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerTest {

    private static final String USER_HEADER = "X-Sharer-User-Id";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private RequestService service;

    private ItemRequestDto inputDTO;
    private ItemRequestDto outputDTO;
    private final Long userId = 1L;
    private final Long requestId = 1L;
    private final Long invalidId = 999L;
    private final LocalDateTime now = LocalDateTime.now();
    private final NotFoundException notFoundException = new NotFoundException("Exception");

    @BeforeEach
    void setUp() {

        inputDTO = ItemRequestDto.builder()
                .description("Хотел бы воспользоваться щёткой для обуви")
                .build();

        outputDTO = ItemRequestDto.builder()
                .id(1L)
                .description("Хотел бы воспользоваться щёткой для обуви")
                .created(now)
                .items(new ArrayList<>())
                .build();
    }

    @Test
    @SneakyThrows
    void testCreateItem_ResulStatusCreated() {
        when(service.create(anyLong(), any(ItemRequestDto.class))).thenReturn(outputDTO);

        mvc.perform(post("/requests")
                        .header(USER_HEADER, userId)
                        .content(mapper.writeValueAsString(inputDTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(outputDTO)));

        verify(service, times(1)).create(anyLong(), any(ItemRequestDto.class));
    }

    @Test
    @SneakyThrows
    void testCreateItem_WithEmptyDescription_ResulStatusBadRequest() {
        inputDTO.setDescription(null);

        mvc.perform(post("/requests")
                        .header(USER_HEADER, userId)
                        .content(mapper.writeValueAsString(inputDTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        MethodArgumentNotValidException.class));

        verify(service, never()).create(anyLong(), any(ItemRequestDto.class));
    }

    @Test
    @SneakyThrows
    void testGetItemRequest_ById_ResultStatusOk() {
        when(service.getByRequestId(anyLong(), anyLong())).thenReturn(outputDTO);

        mvc.perform(get("/requests/{requestId}", requestId)
                        .header(USER_HEADER, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(outputDTO)));

        verify(service, times(1)).getByRequestId(userId, requestId);
    }

    @Test
    @SneakyThrows
    void testGetItemRequest_ByInvalidId_ResultStatusNotFound() {
        when(service.getByRequestId(anyLong(), anyLong())).thenThrow(notFoundException);

        mvc.perform(get("/requests/{requestId}", invalidId)
                        .header(USER_HEADER, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        NotFoundException.class));

        verify(service, times(1)).getByRequestId(userId, invalidId);
    }

    @Test
    @SneakyThrows
    void testGetItemRequest_ByInvalidUserId_ResultStatusNotFound() {
        when(service.getByRequestId(anyLong(), anyLong())).thenThrow(notFoundException);

        mvc.perform(get("/requests/{requestId}", requestId)
                        .header(USER_HEADER, invalidId))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals(Objects.requireNonNull(result.getResolvedException()).getClass(),
                        NotFoundException.class));

        verify(service, times(1)).getByRequestId(invalidId, requestId);
    }

    @Test
    @SneakyThrows
    void testGetAllItemRequest_ResultStatusOk() {
        ItemRequestDto itemRequestOutputDTO1 = ItemRequestDto.builder()
                .id(1L)
                .description("Хотел бы воспользоваться щёткой для обуви")
                .created(now.minusHours(1))
                .items(new ArrayList<>())
                .build();

        ItemRequestDto itemRequestOutputDTO2 = ItemRequestDto.builder()
                .id(2L)
                .description("Нужна отвертка")
                .created(now)
                .items(new ArrayList<>())
                .build();

        List<ItemRequestDto> itemRequests = Arrays.asList(itemRequestOutputDTO1, itemRequestOutputDTO2);

        when(service.getAll(anyLong(), anyInt(), anyInt())).thenReturn(itemRequests);

        int from = 0;
        int size = 2;
        mvc.perform(get("/requests/all")
                        .header(USER_HEADER, userId)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemRequests)));

        verify(service, times(1)).getAll(userId, from, size);
    }


    @Test
    @SneakyThrows
    void testGetAllByRequesterId_ResultStatusOk() {
        ItemRequestDto itemRequestOutputDTO1 = ItemRequestDto.builder()
                .id(1L)
                .description("Хотел бы воспользоваться щёткой для обуви")
                .created(now.minusHours(1))
                .items(new ArrayList<>())
                .build();

        ItemRequestDto itemRequestOutputDTO2 = ItemRequestDto.builder()
                .id(2L)
                .description("Нужна отвертка")
                .created(now)
                .items(new ArrayList<>())
                .build();

        List<ItemRequestDto> itemRequests = Arrays.asList(itemRequestOutputDTO1, itemRequestOutputDTO2);

        when(service.getAllByRequesterId(anyLong())).thenReturn(itemRequests);

        mvc.perform(get("/requests")
                        .header(USER_HEADER, userId))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemRequests)));

        verify(service, times(1)).getAllByRequesterId(userId);
    }
}