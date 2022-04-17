package ru.otus.dto;

import ru.otus.model.Client;

public class ClientDto {
    private final String fullName;
    private final String address;
    private final String phone;

    public ClientDto(Client client) {
        this.fullName = client.getName();
        this.address = client.getAddress() != null ? client.getAddress().getStreet() : "";
        this.phone = client.getPhones().isEmpty() ? "" : client.getPhones().get(0).getNumber();
    }

    public String getFullName() {
        return fullName;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }
}
