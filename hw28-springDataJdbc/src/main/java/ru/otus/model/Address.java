package ru.otus.model;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Table("address")
public class Address {

    @Id
    private final Long id;

    private final String street;

    @PersistenceConstructor
    public Address(Long id, String street) {
        this.id = id;
        this.street = street;
    }

    public Address(String street) {
        this(null, street);
    }
}
