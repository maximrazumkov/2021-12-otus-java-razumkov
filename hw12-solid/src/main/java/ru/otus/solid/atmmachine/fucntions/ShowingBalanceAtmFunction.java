package ru.otus.solid.atmmachine.fucntions;

import ru.otus.solid.atmmachine.models.Money;
import ru.otus.solid.atmmachine.stores.AtmStore;

import java.util.function.Supplier;

public class ShowingBalanceAtmFunction implements AtmFunction<Object, Integer> {

    private final AtmStore<Money> atmStore;

    public ShowingBalanceAtmFunction(AtmStore<Money> atmStore) {
        this.atmStore = atmStore;
    }

    @Override
    public Supplier<Integer> run(Supplier<Object> parameters) {
        return atmStore::getBalance;
    }
}
