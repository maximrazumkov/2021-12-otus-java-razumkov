package ru.otus.solid.atmmachine.models;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class Money {
    private final Map<Banknote, Integer> banknotes;

    public Money(Map<Banknote, Integer> banknotes) {
        this.banknotes = banknotes;
    }

    @Override
    public String toString() {
        return banknotes.entrySet().stream()
                .filter(entry -> entry.getValue() > 0)
                .map(entry -> String.format("%s - %s", entry.getKey().getCode(), entry.getValue()))
                .collect(Collectors.joining("\n"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return Objects.equals(banknotes, money.banknotes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(banknotes);
    }
}
