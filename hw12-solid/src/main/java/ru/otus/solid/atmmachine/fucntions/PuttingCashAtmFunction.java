package ru.otus.solid.atmmachine.fucntions;

import ru.otus.solid.atmmachine.models.Banknote;
import ru.otus.solid.atmmachine.models.Money;
import ru.otus.solid.atmmachine.stores.AtmStore;

import java.util.List;
import java.util.function.Supplier;

public class PuttingCashAtmFunction implements AtmFunction<Integer, Integer> {

    private static final String TITLE = "Внесение наличных.";
    public final static String SUM_SHOULD_NOT_ZERO = "Сумма не может равняться нулю.";
    public final static String SUM_NOT_CORRECT = "Сумму неккоректна. Повторите операцию с корректной суммой";

    private final AtmStore<Money> atmStore;

    public PuttingCashAtmFunction(AtmStore<Money> atmStore) {
        this.atmStore = atmStore;
    }

    @Override
    public Supplier<Integer> run(Supplier<Integer> parameters) {
        int sum = parameters.get();
        validSum(sum);
        atmStore.putMoney(sum);
        return () -> atmStore.getBalance();
    }

    private void validSum(int sum) {
        if (sum == 0) {
            throw new RuntimeException(SUM_SHOULD_NOT_ZERO);
        }
        List<Banknote> banknotes = Banknote.sortedByDesc();
        int errSum = sum;
        for (Banknote banknote: banknotes) {
            errSum %= Integer.parseInt(banknote.getCode());
        }
        if (errSum != 0) {
            throw new RuntimeException(SUM_NOT_CORRECT);
        }
    }

    @Override
    public String toString() {
        return TITLE;
    }
}
