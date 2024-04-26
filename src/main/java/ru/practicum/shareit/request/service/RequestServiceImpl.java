package ru.practicum.shareit.request.service;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.repository.ItemRequestRepository;


@Service
public class RequestServiceImpl implements RequestService {

    private ItemRequestRepository repository;

    public RequestServiceImpl(ItemRequestRepository repository) {
        this.repository = repository;
    }
}
