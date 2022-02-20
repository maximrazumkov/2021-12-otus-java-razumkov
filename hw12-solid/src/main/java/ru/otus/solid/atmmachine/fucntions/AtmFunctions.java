package ru.otus.solid.atmmachine.fucntions;

public interface AtmFunctions<T, R extends AtmFunction> {
    R getFunction(T functionId);
}
