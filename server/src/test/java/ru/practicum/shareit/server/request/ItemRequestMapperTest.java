package ru.practicum.shareit.server.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.server.request.dto.ItemRequestDto;
import ru.practicum.shareit.server.request.dto.ItemRequestMapperImpl;
import ru.practicum.shareit.server.request.model.ItemRequest;
import ru.practicum.shareit.server.user.model.User;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
public class ItemRequestMapperTest {

    @InjectMocks
    private ItemRequestMapperImpl itemRequestMapper;

    @Test
    public void testInputDTOToEntity_Mapping() {

        ItemRequestDto inputDTO = ItemRequestDto.builder()
                .id(1L)
                .description("Нужен диван.")
                .requesterId(2L)
                .build();

        ItemRequest itemRequest = itemRequestMapper.toEntity(inputDTO);

        assertThat(itemRequest).isNotNull();
        assertThat(itemRequest.getId()).isEqualTo(inputDTO.getId());
        assertThat(itemRequest.getDescription()).isEqualTo(inputDTO.getDescription());
    }

    @Test
    public void testInputDTOToEntity_ReturnNull() {

        assertNull(itemRequestMapper.toEntity(null));
    }

    @Test
    public void testToOutputDTO_Mapping() {
        User requester = User.builder()
                .id(2L)
                .build();
        ItemRequest itemRequest = ItemRequest.builder()
                .id(1L)
                .description("Нужен диван.")
                .requester(requester)
                .build();

        ItemRequestDto outputDTO = itemRequestMapper.toDto(itemRequest);

        assertThat(outputDTO).isNotNull();
        assertThat(outputDTO.getId()).isEqualTo(itemRequest.getId());
        assertThat(outputDTO.getDescription()).isEqualTo(itemRequest.getDescription());
    }

    @Test
    public void testToOutputDTOs_Mapping() {
        ItemRequest itemRequest1 = ItemRequest.builder().id(1L).build();
        ItemRequest itemRequest2 = ItemRequest.builder().id(2L).build();
        List<ItemRequest> entities = Arrays.asList(itemRequest1, itemRequest2);

        List<ItemRequestDto> result = itemRequestMapper.toIDtoList(entities);

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
    }

    @Test
    public void testToOutputDTOs_ReturnNull() {

        assertNull(itemRequestMapper.toIDtoList(null));
    }

}
