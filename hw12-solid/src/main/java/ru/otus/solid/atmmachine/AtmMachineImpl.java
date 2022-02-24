package ru.otus.solid.atmmachine;

import ru.otus.solid.atmmachine.fucntions.AtmFunction;
import ru.otus.solid.atmmachine.fucntions.AtmFunctions;

import java.util.function.Supplier;

public class AtmMachineImpl implements AtmMachine {

    private final AtmFunctions<Integer, AtmFunction> atmFunctions;

    public AtmMachineImpl(AtmFunctions<Integer, AtmFunction> atmFunctions) {
        this.atmFunctions = atmFunctions;
    }

    @Override
    public Supplier<?> start(int functionId, Supplier<?> parameters) {
        AtmFunction atmFunction = atmFunctions.getFunction(functionId);
        return atmFunction.run(parameters);
    }
}
