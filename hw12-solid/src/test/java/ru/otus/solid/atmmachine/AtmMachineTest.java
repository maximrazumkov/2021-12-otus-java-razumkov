package ru.otus.solid.atmmachine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Класс AtmMachineImpl")
public class AtmMachineTest {

    private AtmMachine atmMachine;

    private Map<Banknote, Integer> banknoteMap;

    @BeforeEach
    public void initAtmMachine() {
        banknoteMap = new HashMap<>();
        banknoteMap.put(Banknote.ONE_HUNDRED, 10);
        banknoteMap.put(Banknote.FIVE_HUNDREDS, 10);
        banknoteMap.put(Banknote.ONE_THOUSAND, 5);
        atmMachine = new MapMemoryAtmMachine(banknoteMap);
    }

    @Test
    @DisplayName("Должен принимать деньги разного номинала")
    public void shouldTakeDifferentMoney() {
        atmMachine.putMoney(1900);
        assertThat(atmMachine.getBalance()).isEqualTo(12_900);
    }

    @Test
    @DisplayName("Должен выдать запрашиваемую сумму минимальным количесвтом банкнот")
    public void shouldGiveMinBanknoteMoneyBySum() {
        Map<Banknote, Integer> banknoteMap = new HashMap<>();
        banknoteMap.put(Banknote.ONE_THOUSAND, 1);
        banknoteMap.put(Banknote.FIVE_HUNDREDS, 1);
        banknoteMap.put(Banknote.ONE_HUNDRED, 4);
        Map<Banknote, Integer> money = atmMachine.getMoneyBySum(1900);
        assertThat(atmMachine.getBalance()).isEqualTo(9100);
        assertThat(money).isEqualTo(banknoteMap);
    }

    @Test
    @DisplayName("Должен показать остаток денежных стредств")
    public void shouldShowCashBalance() {
        assertThat(atmMachine.getBalance()).isEqualTo(11_000);
    }

    @Test()
    @DisplayName("Должен выдать исключение, если cумма превышает допустимый баланс")
    public void shouldThrowExceptionWhenSumMoreThanBalance() {
        assertThrows(RuntimeException.class, () -> atmMachine.getMoneyBySum(99_999));
    }

    @Test()
    @DisplayName("Должен выдать исключение, если cумма некрастна допустимой")
    public void shouldThrowExceptionWhenSumNotCorrect() {
        assertThrows(RuntimeException.class, () -> atmMachine.getMoneyBySum(1050));
    }

    @Test()
    @DisplayName("Должен выдать исключение, если баланс равен 0")
    public void shouldThrowExceptionWhenBalanceIsZero() {
        atmMachine = new MapMemoryAtmMachine(new HashMap<>());
        assertThrows(RuntimeException.class, () -> atmMachine.getMoneyBySum(1000));
    }
}
