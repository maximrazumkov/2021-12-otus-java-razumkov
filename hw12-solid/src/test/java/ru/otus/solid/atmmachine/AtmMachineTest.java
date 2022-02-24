package ru.otus.solid.atmmachine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.solid.atmmachine.fucntions.*;
import ru.otus.solid.atmmachine.models.Banknote;
import ru.otus.solid.atmmachine.models.Money;
import ru.otus.solid.atmmachine.stores.AtmStore;
import ru.otus.solid.atmmachine.stores.AtmStoreImpl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.otus.solid.atmmachine.fucntions.AtmFunctionsMap.NOT_SUPPORT_FUNCTION;
import static ru.otus.solid.atmmachine.fucntions.GettingCashAtmMachine.BALANCE_IS_EMPTY;
import static ru.otus.solid.atmmachine.fucntions.PuttingCashAtmFunction.SUM_NOT_CORRECT;
import static ru.otus.solid.atmmachine.stores.AtmStoreImpl.SUM_MORE_BALANCE;

@DisplayName("Класс AtmMachineImpl")
public class AtmMachineTest {

    private AtmMachine atmMachine;

    private AtmStore<Money> atmStore;

    private Map<Banknote, Integer> banknoteMap;

    private AtmFunctions<Integer, AtmFunction> atmFunctions;

    @BeforeEach
    public void initAtmMachine() {
        banknoteMap = new HashMap<>();
        banknoteMap.put(Banknote.ONE_HUNDRED, 10);
        banknoteMap.put(Banknote.FIVE_HUNDREDS, 10);
        banknoteMap.put(Banknote.ONE_THOUSAND, 5);
        atmStore = new AtmStoreImpl(banknoteMap);
        AtmFunction showingBalance = new ShowingBalanceAtmFunction(atmStore);
        AtmFunction puttingCash = new PuttingCashAtmFunction(atmStore);
        AtmFunction gettingCash = new GettingCashAtmMachine(atmStore);
        Map<Integer, AtmFunction> atmFunctionMap = new LinkedHashMap<>();
        atmFunctionMap.put(1, showingBalance);
        atmFunctionMap.put(2, puttingCash);
        atmFunctionMap.put(3, gettingCash);
        atmFunctions = new AtmFunctionsMap<>(atmFunctionMap);
        atmMachine = new AtmMachineImpl(atmFunctions);
    }

    @Test
    @DisplayName("Должен принимать деньги разного номинала")
    public void shouldTakeDifferentMoney() {
        atmMachine.start(2, () -> 1900);
        assertThat(atmStore.getBalance() == 17400);
    }

    @Test
    @DisplayName("Должен выдать запрашиваемую сумму минимальным количесвтом банкнот")
    public void shouldGiveMinBanknoteMoneyBySum() {
        atmMachine.start(3, () -> 10000);
        assertThat(atmStore.getBalance() == 5500);
    }

    @Test
    @DisplayName("Должен показать остаток денежных стредств")
    public void shouldShowCashBalance() {
        atmMachine.start(1, () -> null);
        int balance = atmStore.getBalance();
        assertThat(balance == 10500);
    }

    @Test()
    @DisplayName("Должен выдать исключение, если функция отсутсвует")
    public void shouldThrowExceptionWhenFunctionIsNot() {
        assertThrows(RuntimeException.class, () -> atmMachine.start(1000, () -> 10000), NOT_SUPPORT_FUNCTION);
    }

    @Test()
    @DisplayName("Должен выдать исключение, если cумма превышает допустимый баланс")
    public void shouldThrowExceptionWhenSumMoreThanBalance() {
        String errMessage = String.format(SUM_MORE_BALANCE, 99999, atmStore.getBalance());
        assertThrows(RuntimeException.class, () -> atmMachine.start(3, () -> 99999), errMessage);
    }

    @Test()
    @DisplayName("Должен выдать исключение, если cумма некрастна допустимой")
    public void shouldThrowExceptionWhenSumNotCorrect() {
        assertThrows(RuntimeException.class, () -> atmMachine.start(3, () -> 1050), SUM_NOT_CORRECT);
    }

    @Test()
    @DisplayName("Должен выдать исключение, если баланс равен 0")
    public void shouldThrowExceptionWhenBalanceIsZero() {
        atmStore.getMoneyBySum(10500);
        assertThrows(RuntimeException.class, () -> atmMachine.start(3, () -> 5000), BALANCE_IS_EMPTY);
    }
}
