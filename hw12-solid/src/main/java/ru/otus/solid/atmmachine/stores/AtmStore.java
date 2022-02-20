package ru.otus.solid.atmmachine.stores;

import ru.otus.solid.atmmachine.models.Money;

public interface AtmStore<T extends Money> {
    T getMoneyBySum(int sum);
    int getBalance();
    void putMoney(int sum);
    int getPossibleAmount();
}
