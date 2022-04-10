package ru.otus.crm.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "phone")
public class Phone implements Cloneable, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "number")
    private String number;

    @JoinColumn(name = "client_id")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Client client;

    public Phone() {
    }

    public Phone(Long id, String number) {
        this.id = id;
        this.number = number;
    }

    public Phone(Long id, String number, Client client) {
        this.id = id;
        this.number = number;
        this.client = client;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public Phone clone() {
        return new Phone(this.id, this.number);
    }
}
