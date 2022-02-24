package ru.otus.solid.atmmachine.fucntions;

import ru.otus.solid.atmmachine.models.Money;
import ru.otus.solid.atmmachine.stores.AtmStore;

import java.util.function.Supplier;

public class GettingCashAtmMachine implements AtmFunction<Integer, Money> {

    public static final String BALANCE_IS_EMPTY = "В банкомате кончились купюры.";
    public static final String SUM_SHOULD_NOT_ZERO = "Нельзя запросить сумму равную: %s";
    public final static String SUM_MORE_BALANCE = "Запрашиваемая сумма %s превышает баланс %s";
    public final static String SUM_NOT_CORRECT = "Введенная сумма %s некратна: %s";

    private final AtmStore<Money> atmStore;

    public GettingCashAtmMachine(AtmStore<Money> atmStore) {
        this.atmStore = atmStore;
    }

    @Override
    public Supplier<Money> run(Supplier<Integer> parameters) {
        if (atmStore.getBalance() == 0) {
            throw new RuntimeException(BALANCE_IS_EMPTY);
        }
        int sum = parameters.get();
        validSum(sum);
        return () -> atmStore.getMoneyBySum(sum);
    }

    private void validSum(int sum) {
        if (sum == 0) {
            throw new RuntimeException(String.format(SUM_SHOULD_NOT_ZERO, sum));
        }
        if (atmStore.getBalance() < sum) {
            throw new RuntimeException(String.format(SUM_MORE_BALANCE, sum, atmStore.getBalance()));
        }
        int remaining = sum % atmStore.getPossibleAmount();
        if (remaining != 0) {
            throw new RuntimeException(String.format(SUM_NOT_CORRECT, sum, atmStore.getPossibleAmount()));
        }
    }
}
