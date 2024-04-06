package ru.practicum.shareit.booking.service;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.repository.BookingRepository;


@Repository
public class BookingServiceImpl implements BookingService {

    @Setter
    @Autowired
    private BookingRepository repository;

    @Override
    public void addffd() {

    }
}
