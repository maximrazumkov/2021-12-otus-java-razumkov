package ru.otus.solid.atmmachine;

import ru.otus.solid.atmmachine.billacceptor.InputConsoleBillAcceptor;
import ru.otus.solid.atmmachine.billacceptor.OutputConsoleBillAcceptor;
import ru.otus.solid.atmmachine.fucntions.*;
import ru.otus.solid.atmmachine.io.*;
import ru.otus.solid.atmmachine.models.Banknote;
import ru.otus.solid.atmmachine.models.Money;
import ru.otus.solid.atmmachine.stores.AtmStore;
import ru.otus.solid.atmmachine.stores.AtmStoreImpl;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class App {
    public static void main(String[] args) {
        Function<String, Integer> сonverterInputInt = new ConverterInputInt();
        Output output = new ConsoleOutput();
        Input input = new ScannerInput(new Scanner(System.in));
        Supplier<Integer> inBillAcceptor = new InputConsoleBillAcceptor(сonverterInputInt, input);
        Consumer<Money> outBillAcceptor = new OutputConsoleBillAcceptor(output);
        Map<Banknote, Integer> map = new HashMap<>();
        map.put(Banknote.ONE_HUNDRED, 15);
        map.put(Banknote.FIVE_HUNDREDS, 15);
        map.put(Banknote.ONE_THOUSAND, 5);
        AtmStore<Money> atmStore = new AtmStoreImpl(map);
        AtmFunction showingBalance = new ShowingBalanceAtmFunction(atmStore, output);
        AtmFunction puttingCash = new PuttingCashAtmFunction(atmStore, inBillAcceptor, output);
        AtmFunction gettingCash = new GettingCashAtmMachine(atmStore, outBillAcceptor, сonverterInputInt, output, input);
        Map<Integer, AtmFunction> atmFunctionMap = new LinkedHashMap<>();
        atmFunctionMap.put(1, showingBalance);
        atmFunctionMap.put(2, puttingCash);
        atmFunctionMap.put(3, gettingCash);
        AtmFunctions<Integer, AtmFunction> atmFunctions = new AtmFunctionsMap<>(atmFunctionMap);
        AtmMachine atmMachine = new AtmMachineImpl(atmFunctions, сonverterInputInt, input, output);
        atmMachine.start();
    }
}
