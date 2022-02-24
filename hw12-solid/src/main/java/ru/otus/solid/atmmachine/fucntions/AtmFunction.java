package ru.otus.solid.atmmachine.fucntions;

import java.util.function.Supplier;

public interface AtmFunction<T, R> {
    Supplier<R> run(Supplier<T> parameters);
}
