package ru.otus.solid.atmmachine;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapMemoryAtmMachine implements AtmMachine {

    public final static String SUM_MORE_BALANCE = "Запрашиваемая сумма %s превышает баланс %s";
    public final static String OPERATION_ERROR = "Ошибка операции. Повторите попытку позже.";
    public final static String SUM_SHOULD_NOT_ZERO = "Нельзя запросить сумму равную: %s";
    public final static String SUM_NOT_CORRECT = "Введенная сумма %s некратна: %s";

    private final Map<Banknote, Integer> money;

    public MapMemoryAtmMachine(Map<Banknote, Integer> money) {
        this.money = money;
    }

    @Override
    public Map<Banknote, Integer> getMoneyBySum(int sum) {
        validGettingSum(sum);
        Map<Banknote, Integer> moneyResult = new HashMap<>();
        int remainingSum = sum;
        List<Banknote> banknotes = Banknote.sortedByDesc();
        for (Banknote banknote : banknotes) {
            if (money.containsKey(banknote)) {
                remainingSum = divideMoney(moneyResult, banknote, remainingSum);
            }
        }
        return moneyResult;
    }

    private void validGettingSum(int sum) {
        if (sum == 0) {
            throw new RuntimeException(String.format(SUM_SHOULD_NOT_ZERO, sum));
        }
        int balance = getBalance();
        if (balance < sum) {
            throw new RuntimeException(String.format(SUM_MORE_BALANCE, sum, balance));
        }
        int possibleAmt = getPossibleAmount();
        int remaining = sum % possibleAmt;
        if (remaining != 0) {
            throw new RuntimeException(String.format(SUM_NOT_CORRECT, sum, possibleAmt));
        }
    }

    private int divideMoney(Map<Banknote, Integer> moneyResult, Banknote banknote, int sum) {
        if (sum == 0) {
            return sum;
        }
        int nominal = banknote.getCode();
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
                .mapToInt(entry -> entry.getValue() * entry.getKey().getCode())
                .reduce(0, Integer::sum);
    }

    @Override
    public void putMoney(int sum) {
        validPuttingSum(sum);
        List<Banknote> banknotes = Banknote.sortedByDesc();
        int remainingSum = sum;
        for (Banknote banknote : banknotes) {
            remainingSum = sumMoney(banknote, remainingSum);
        }
    }

    private void validPuttingSum(int sum) {
        if (sum == 0) {
            throw new RuntimeException(OPERATION_ERROR);
        }
        List<Banknote> banknotes = Banknote.sortedByDesc();
        int errSum = sum;
        for (Banknote banknote: banknotes) {
            errSum %= banknote.getCode();
        }
        if (errSum != 0) {
            throw new RuntimeException(SUM_NOT_CORRECT);
        }
    }

    private int sumMoney(Banknote banknote, int sum) {
        if (!this.money.containsKey(banknote)) {
            this.money.put(banknote, 0);
        }
        int nominal = banknote.getCode();
        int banknoteCount = sum / nominal;
        if (banknoteCount > 0) {
            int remainingBanknotes = this.money.get(banknote);
            this.money.put(banknote, remainingBanknotes + banknoteCount);
            sum -= banknoteCount * nominal;
        }
        return sum;
    }

    private int getPossibleAmount() {
        return money.entrySet().stream()
                .filter(value -> value.getValue() > 0)
                .min(Comparator.comparingInt(value -> value.getKey().getCode()))
                .map(entry -> entry.getKey().getCode())
                .orElse(0);
    }
}
