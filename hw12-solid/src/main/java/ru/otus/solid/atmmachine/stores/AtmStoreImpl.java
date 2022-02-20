package ru.otus.solid.atmmachine.stores;

import ru.otus.solid.atmmachine.models.Banknote;
import ru.otus.solid.atmmachine.models.Money;

import java.util.*;

public class AtmStoreImpl implements AtmStore<Money> {

    public final static String SUM_MORE_BALANCE = "Запрашиваемая сумма %s превышает баланс %s";
    public final static String OPERATION_ERROR = "Ошибка операции. Повторите попытку позже.";

    private final Map<Banknote, Integer> money;

    public AtmStoreImpl(Map<Banknote, Integer> money) {
        this.money = money;
    }

    @Override
    public Money getMoneyBySum(int sum) {
        if (getBalance() < sum) {
            throw new RuntimeException(String.format(SUM_MORE_BALANCE, sum, getBalance()));
        }
        Map<Banknote, Integer> moneyResult = new HashMap<>();
        int remainingSum = sum;
        List<Banknote> banknotes = Banknote.sortedByDesc();
        for (Banknote banknote : banknotes) {
            if (money.containsKey(banknote)) {
                remainingSum = divideMoney(moneyResult, banknote, remainingSum);
            }
        }
        return new Money(moneyResult);
    }

    private int divideMoney(Map<Banknote, Integer> moneyResult, Banknote banknote, int sum) {
        if (sum == 0) {
            return sum;
        }
        int nominal = Integer.parseInt(banknote.getCode());
        int banknotes = sum / nominal;
        int remaining = money.get(banknote);
        int div = remaining - banknotes;
        if (div < 0) {
            moneyResult.put(banknote, remaining);
            money.put(banknote, 0);
            sum -= remaining * nominal;
        } else {
            moneyResult.put(banknote, banknotes);
            money.put(banknote, div);
            sum -= banknotes * nominal;
        }
        return sum;
    }

    @Override
    public int getBalance() {
        return money.entrySet().stream()
                .mapToInt(entry -> entry.getValue() * Integer.parseInt(entry.getKey().getCode()))
                .reduce(0, Integer::sum);
    }

    @Override
    public void putMoney(int sum) {
        if (sum == 0) {
            throw new RuntimeException(OPERATION_ERROR);
        }
        List<Banknote> banknotes = Banknote.sortedByDesc();
        int remainingSum = sum;
        for (Banknote banknote : banknotes) {
            remainingSum = sumMoney(banknote, remainingSum);
        }
    }

    private int sumMoney(Banknote banknote, int sum) {
        if (!this.money.containsKey(banknote)) {
            this.money.put(banknote, 0);
        }
        int nominal = Integer.parseInt(banknote.getCode());
        int banknoteCount = sum / nominal;
        if (banknoteCount > 0) {
            int remainingBanknotes = this.money.get(banknote);
            this.money.put(banknote, remainingBanknotes + banknoteCount);
            sum -= banknoteCount * nominal;
        }
        return sum;
    }

    @Override
    public int getPossibleAmount() {
        return money.entrySet().stream()
                .filter(value -> value.getValue() > 0)
                .min(Comparator.comparingInt(value -> Integer.parseInt(value.getKey().getCode())))
                .map(entry -> Integer.valueOf(entry.getKey().getCode()))
                .orElse(0);
    }
}
