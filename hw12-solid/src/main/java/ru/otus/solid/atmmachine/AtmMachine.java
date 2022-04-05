package ru.otus.solid.atmmachine;

import java.util.Map;

public interface AtmMachine {
    Map<Banknote, Integer> getMoneyBySum(int sum);
    int getBalance();
    void putMoney(int sum);
}
