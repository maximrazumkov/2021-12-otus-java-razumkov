package ru.otus.service;

import ru.otus.dto.ClientDto;

import java.util.List;

public interface ClientService {
    List<ClientDto> findAll();
    void save(ClientDto clientDto);
}
