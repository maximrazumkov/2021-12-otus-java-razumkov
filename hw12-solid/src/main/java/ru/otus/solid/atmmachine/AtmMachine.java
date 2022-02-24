package ru.otus.solid.atmmachine;

import java.util.function.Supplier;

public interface AtmMachine {
    Supplier<?> start(int functionId, Supplier<?> parameters);
}
