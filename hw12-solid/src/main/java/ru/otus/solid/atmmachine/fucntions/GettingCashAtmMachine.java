package ru.otus.solid.atmmachine.fucntions;

import ru.otus.solid.atmmachine.io.Input;
import ru.otus.solid.atmmachine.io.Output;
import ru.otus.solid.atmmachine.models.Money;
import ru.otus.solid.atmmachine.stores.AtmStore;

import java.util.function.Consumer;
import java.util.function.Function;

public class GettingCashAtmMachine implements AtmFunction {

    private static final String TITLE = "Выдача наличных.";
    public static final String BALANCE_IS_EMPTY = "В банкомате кончились купюры.";
    public static final String INPUT_MONEY = "Введите запрашиваемую сумму кратную: %s";
    public static final String SUM_SHOULD_NOT_ZERO = "Нельзя запросить сумму равную: %s";
    public final static String SUM_MORE_BALANCE = "Запрашиваемая сумма %s превышает баланс %s";
    public final static String SUM_NOT_CORRECT = "Введенная сумма %s некратна: %s";

    private final AtmStore<Money> atmStore;
    private final Consumer<Money> billAcceptor;
    private final Function<String, Integer> convertStringToInt;
    private final Output output;
    private final Input input;

    public GettingCashAtmMachine(
            AtmStore<Money> atmStore,
            Consumer<Money> billAcceptor,
            Function<String, Integer> convertStringToInt,
            Output output,
            Input input
    ) {
        this.atmStore = atmStore;
        this.billAcceptor = billAcceptor;
        this.convertStringToInt = convertStringToInt;
        this.output = output;
        this.input = input;
    }

    @Override
    public void run() {
        if (atmStore.getBalance() == 0) {
            throw new RuntimeException(BALANCE_IS_EMPTY);
        }
        output.print(String.format(INPUT_MONEY, atmStore.getPossibleAmount()));
        int sum = convertStringToInt.apply(input.readLine());
        validSum(sum);
        Money money = atmStore.getMoneyBySum(sum);
        billAcceptor.accept(money);
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

    @Override
    public String toString() {
        return TITLE;
    }
}
