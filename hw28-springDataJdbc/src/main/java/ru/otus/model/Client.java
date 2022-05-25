package ru.otus.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Set;

@Data
@Table("client")
public class Client {

    @Id
    private final Long id;

    private final String name;

    private final Long addressId;

    @MappedCollection(idColumn = "client_id")
    private final Set<Phone> phones;

    public Client(String name, Long addressId, Set<Phone> phones) {
        this(null, name, addressId, phones);
    }

    @PersistenceConstructor
    public Client(Long id, String name, Long addressId, Set<Phone> phones) {
        this.id = id;
        this.name = name;
        this.addressId = addressId;
        this.phones = phones;
    }
}
