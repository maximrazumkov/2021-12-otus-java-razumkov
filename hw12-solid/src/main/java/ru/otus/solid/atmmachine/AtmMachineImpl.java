package ru.otus.solid.atmmachine;

import ru.otus.solid.atmmachine.fucntions.AtmFunction;
import ru.otus.solid.atmmachine.fucntions.AtmFunctions;
import ru.otus.solid.atmmachine.io.Input;
import ru.otus.solid.atmmachine.io.Output;

import java.util.function.Function;

public class AtmMachineImpl implements AtmMachine {

    private static final String MENU = "Выберите номер:";

    private final AtmFunctions<Integer, AtmFunction> atmFunctions;
    private final Function<String, Integer> сonverterInputInt;
    private final Input input;
    private final Output output;

    public AtmMachineImpl(
            AtmFunctions<Integer, AtmFunction> atmFunctions,
            Function<String, Integer> сonverterInputInt,
            Input input, Output output
    ) {
        this.atmFunctions = atmFunctions;
        this.сonverterInputInt = сonverterInputInt;
        this.input = input;
        this.output = output;
    }

    @Override
    public void start() {
        output.print(MENU);
        output.print(atmFunctions.toString());
        Integer functionId = сonverterInputInt.apply(input.readLine());
        AtmFunction atmFunction = atmFunctions.getFunction(functionId);
        atmFunction.run();
    }
}
