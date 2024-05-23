package ru.practicum.shareit.server.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.server.request.dto.ItemRequestDto;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemDTOTest {
    @Autowired
    private JacksonTester<ItemRequestDto> json;

    private final String content = "{\"description\":\"Хотел бы воспользоваться щёткой для обуви\",\"requesterId\":1}";
    private final ItemRequestDto itemRequestInputDTO = ItemRequestDto.builder()
            .description("Хотел бы воспользоваться щёткой для обуви")
            .requesterId(1L)
            .build();

    @Test
    public void testJsonDeserialization() throws Exception {

        ItemRequestDto result = json.parse(content).getObject();

        assertThat(result.getDescription()).isEqualTo(itemRequestInputDTO.getDescription());
        assertThat(result.getRequesterId()).isEqualTo(itemRequestInputDTO.getRequesterId());
    }
}
