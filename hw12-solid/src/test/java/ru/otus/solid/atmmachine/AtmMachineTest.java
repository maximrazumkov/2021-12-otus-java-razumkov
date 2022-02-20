package ru.otus.solid.atmmachine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.InOrder;
import ru.otus.solid.atmmachine.billacceptor.InputConsoleBillAcceptor;
import ru.otus.solid.atmmachine.billacceptor.OutputConsoleBillAcceptor;
import ru.otus.solid.atmmachine.fucntions.*;
import ru.otus.solid.atmmachine.io.*;
import ru.otus.solid.atmmachine.models.Banknote;
import ru.otus.solid.atmmachine.models.Money;
import ru.otus.solid.atmmachine.stores.AtmStore;
import ru.otus.solid.atmmachine.stores.AtmStoreImpl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static ru.otus.solid.atmmachine.fucntions.AtmFunctionsMap.NOT_SUPPORT_FUNCTION;
import static ru.otus.solid.atmmachine.fucntions.GettingCashAtmMachine.BALANCE_IS_EMPTY;
import static ru.otus.solid.atmmachine.fucntions.PuttingCashAtmFunction.SUM_NOT_CORRECT;
import static ru.otus.solid.atmmachine.fucntions.ShowingBalanceAtmFunction.BALANCE;
import static ru.otus.solid.atmmachine.stores.AtmStoreImpl.SUM_MORE_BALANCE;

@DisplayName("Класс AtmMachineImpl")
public class AtmMachineTest {

    private AtmMachine atmMachine;

    private Supplier<Integer> inBillAcceptor;

    private Consumer<Money> outBillAcceptor;

    private InOrder inOurderOutBillAcceptor;

    private Output output;

    private InOrder inOutput;

    private Input input;

    private AtmStore<Money> atmStore;

    private Map<Banknote, Integer> banknoteMap;

    private AtmFunctions<Integer, AtmFunction> atmFunctions;

    @BeforeEach
    public void initAtmMachine() {
        output = BDDMockito.mock(Output.class);
        input = BDDMockito.mock(Input.class);
        inBillAcceptor = BDDMockito.mock(InputConsoleBillAcceptor.class);
        outBillAcceptor = BDDMockito.mock(OutputConsoleBillAcceptor.class);
        inOurderOutBillAcceptor = inOrder(outBillAcceptor);
        inOutput = inOrder(output);
        Function<String, Integer> сonverterInputInt = new ConverterInputInt();
        banknoteMap = new HashMap<>();
        banknoteMap.put(Banknote.ONE_HUNDRED, 10);
        banknoteMap.put(Banknote.FIVE_HUNDREDS, 10);
        banknoteMap.put(Banknote.ONE_THOUSAND, 5);
        atmStore = new AtmStoreImpl(banknoteMap);
        AtmFunction showingBalance = new ShowingBalanceAtmFunction(atmStore, output);
        AtmFunction puttingCash = new PuttingCashAtmFunction(atmStore, inBillAcceptor, output);
        AtmFunction gettingCash = new GettingCashAtmMachine(atmStore, outBillAcceptor, сonverterInputInt, output, input);
        Map<Integer, AtmFunction> atmFunctionMap = new LinkedHashMap<>();
        atmFunctionMap.put(1, showingBalance);
        atmFunctionMap.put(2, puttingCash);
        atmFunctionMap.put(3, gettingCash);
        atmFunctions = new AtmFunctionsMap<>(atmFunctionMap);
        atmMachine = new AtmMachineImpl(atmFunctions, сonverterInputInt, input, output);
    }

    @Test
    @DisplayName("Должен принимать деньги разного номинала")
    public void shouldTakeDifferentMoney() {
        BDDMockito.given(input.readLine()).willReturn("2");
        BDDMockito.given(inBillAcceptor.get()).willReturn(1900);
        atmMachine.start();
        assertThat(atmStore.getBalance() == 17400);
    }

    @Test
    @DisplayName("Должен выдать запрашиваемую сумму минимальным количесвтом банкнот")
    public void shouldGiveMinBanknoteMoneyBySum() {
        BDDMockito.given(input.readLine()).willReturn("3").willReturn("10000");
        atmMachine.start();
        assertThat(atmStore.getBalance() == 5500);
        Map<Banknote, Integer> result = new HashMap<>();
        result.put(Banknote.ONE_THOUSAND, 5);
        result.put(Banknote.FIVE_HUNDREDS, 10);
        inOurderOutBillAcceptor.verify(outBillAcceptor, times(1)).accept(new Money(result));
    }

    @Test
    @DisplayName("Должен показать остаток денежных стредств")
    public void shouldShowCashBalance() {
        BDDMockito.given(input.readLine()).willReturn("1");
        atmMachine.start();
        int balance = atmStore.getBalance();
        inOutput.verify(output, times(1)).print(String.format(BALANCE, balance));
    }

    @Test()
    @DisplayName("Должен выдать исключение, если функция отсутсвует")
    public void shouldThrowExceptionWhenFunctionIsNot() {
        BDDMockito.given(input.readLine()).willReturn("100");
        assertThrows(RuntimeException.class, () -> atmMachine.start(), NOT_SUPPORT_FUNCTION);
    }

    @Test()
    @DisplayName("Должен выдать исключение, если cумма превышает допустимый баланс")
    public void shouldThrowExceptionWhenSumMoreThanBalance() {
        BDDMockito.given(input.readLine()).willReturn("3").willReturn("99999");
        String errMessage = String.format(SUM_MORE_BALANCE, 99999, atmStore.getBalance());
        assertThrows(RuntimeException.class, () -> atmMachine.start(), errMessage);
    }

    @Test()
    @DisplayName("Должен выдать исключение, если cумма некрастна допустимой")
    public void shouldThrowExceptionWhenSumNotCorrect() {
        BDDMockito.given(input.readLine()).willReturn("3").willReturn("1050");
        assertThrows(RuntimeException.class, () -> atmMachine.start(), SUM_NOT_CORRECT);
    }

    @Test()
    @DisplayName("Должен выдать исключение, если баланс равен 0")
    public void shouldThrowExceptionWhenBalanceIsZero() {
        atmStore.getMoneyBySum(10500);
        BDDMockito.given(input.readLine()).willReturn("3").willReturn("5000");
        assertThrows(RuntimeException.class, () -> atmMachine.start(), BALANCE_IS_EMPTY);
    }
}
