package ru.otus.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.dto.ClientDto;
import ru.otus.model.Address;
import ru.otus.model.Client;
import ru.otus.model.Phone;
import ru.otus.repository.AddressRepository;
import ru.otus.service.ClientService;
import ru.otus.service.DBServiceClient;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final DBServiceClient dbServiceClient;
    private final AddressRepository addressRepository;

    @Override
    public List<ClientDto> findAll() {
        return dbServiceClient.findAll().stream()
                .map(this::convertClientToClientDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void save(ClientDto clientDto) {
        var address = addressRepository.save(new Address(clientDto.getAddress()));
        var phones = Set.of(new Phone(clientDto.getPhone()));
        var client = new Client(clientDto.getFullName(), address.getId(), phones);
        dbServiceClient.saveClient(client);
    }


    private ClientDto convertClientToClientDto(Client client) {
        var phone = client.getPhones().stream().findAny().orElse(new Phone("")).getNumber();
        var address = addressRepository.findById(client.getAddressId()).orElse(new Address(""));
        return new ClientDto(client.getName(), address.getStreet(), phone);
    }
}
