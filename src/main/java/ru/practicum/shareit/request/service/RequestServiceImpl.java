package ru.practicum.shareit.request.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class RequestServiceImpl implements RequestService {

    private final ItemRequestRepository requestRepository;
    private final UserService userService;
    private final ItemRepository itemRepository;

    public RequestServiceImpl(ItemRequestRepository requestRepository, UserService userService, ItemRepository itemRepository) {
        this.requestRepository = requestRepository;
        this.userService = userService;
        this.itemRepository = itemRepository;
    }

    @Override
    @Transactional
    public ItemRequestDto create(Long userId, ItemRequestDto request) {

        UserDto user = userService.getUserById(userId);
        request.setRequesterId(userId);

        ItemRequest itemRequest = ItemRequestMapper.INSTANCE.toEntity(request);
        itemRequest.setRequester(UserMapper.INSTANCE.toDao(user));

        return ItemRequestMapper.INSTANCE.toDto(requestRepository.save(itemRequest));
    }

    @Override
    @Transactional
    public ItemRequestDto getByRequestId(Long userId, Long requestId) {

        userService.getUserById(userId);

        ItemRequestDto outputDTO = ItemRequestMapper.INSTANCE.toDto(requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден")));

        outputDTO.setItems(ItemMapper.INSTANCE.toListDto(itemRepository.findAllByRequestId(requestId)));

        return outputDTO;
    }

    @Override
    @Transactional
    public List<ItemRequestDto> getAllByRequesterId(Long userId) {

        userService.getUserById(userId);
        List<ItemRequest> itemRequests = requestRepository.findAllByRequester_Id(userId);

        List<ItemRequestDto> outputDTOs = ItemRequestMapper.INSTANCE.toIDtoList(itemRequests);
        return setItemsToRequests(outputDTOs);
    }

    @Override
    @Transactional
    public List<ItemRequestDto> getAll(Long userId, Integer from, Integer size) {
        userService.getUserById(userId);
        List<ItemRequestDto> outputDTOs = ItemRequestMapper.INSTANCE.toIDtoList(requestRepository.findAllFromOtherUsers(userId, from, size));

        return setItemsToRequests(outputDTOs);
    }

    private List<ItemRequestDto> setItemsToRequests(List<ItemRequestDto> outputDTOs) {
        List<Long> requestIds = outputDTOs.stream()
                .map(ItemRequestDto::getId)
                .collect(Collectors.toList());

        Map<Long, List<ItemDto>> items = itemRepository
                .findAllByRequest_IdIn(requestIds)
                .stream()
                .map(ItemMapper.INSTANCE::toDto)
                .collect(Collectors.groupingBy(ItemDto::getRequestId));

        return outputDTOs.stream()
                .peek(r -> r.setItems(items.getOrDefault(r.getId(), new ArrayList<>())))
                .collect(Collectors.toList());
    }

}
