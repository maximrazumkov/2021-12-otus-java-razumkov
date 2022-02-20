package ru.otus.solid.atmmachine.fucntions;

import ru.otus.solid.atmmachine.io.Output;
import ru.otus.solid.atmmachine.models.Money;
import ru.otus.solid.atmmachine.stores.AtmStore;

public class ShowingBalanceAtmFunction implements AtmFunction {

    private static final String TITLE = "Показать баланс.";
    public static final String BALANCE = "Баланс на счёте: %s";

    private final AtmStore<Money> atmStore;
    private final Output output;

    public ShowingBalanceAtmFunction(AtmStore<Money> atmStore, Output output) {
        this.atmStore = atmStore;
        this.output = output;
    }

    @Override
    public void run() {
        int balance = atmStore.getBalance();
        output.print(String.format(BALANCE, balance));
    }

    @Override
    public String toString() {
        return TITLE;
    }
}
