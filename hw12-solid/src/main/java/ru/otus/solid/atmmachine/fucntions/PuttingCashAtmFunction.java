package ru.otus.solid.atmmachine.fucntions;

import ru.otus.solid.atmmachine.io.Output;
import ru.otus.solid.atmmachine.models.Banknote;
import ru.otus.solid.atmmachine.models.Money;
import ru.otus.solid.atmmachine.stores.AtmStore;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;

public class PuttingCashAtmFunction implements AtmFunction {

    private static final String TITLE = "Внесение наличных.";
    public static final String INPUT_MONEY = "Введите сумму кратную: %s";
    public static final String ACCOUNT_BALANCE = "Остаток на счете: %s";
    public final static String SUM_SHOULD_NOT_ZERO = "Сумма не может равняться нулю.";
    public final static String SUM_NOT_CORRECT = "Сумму неккоректна. Повторите операцию с корректной суммой";

    private final AtmStore<Money> atmStore;
    private final Supplier<Integer> billAcceptor;
    private final Output output;

    public PuttingCashAtmFunction(AtmStore<Money> atmStore, Supplier<Integer> billAcceptor, Output output) {
        this.atmStore = atmStore;
        this.billAcceptor = billAcceptor;
        this.output = output;
    }

    @Override
    public void run() {
        String min = Arrays.stream(Banknote.values())
                .map(Banknote::getCode)
                .min(Comparator.comparingInt(value -> Integer.parseInt(value)))
                .get();
        output.print(String.format(INPUT_MONEY, min));
        int sum = billAcceptor.get();
        validSum(sum);
        atmStore.putMoney(sum);
        output.print(String.format(ACCOUNT_BALANCE, atmStore.getBalance()));
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
