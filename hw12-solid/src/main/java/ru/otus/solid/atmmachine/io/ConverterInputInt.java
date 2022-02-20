package ru.otus.solid.atmmachine.io;

import java.util.function.Function;

public class ConverterInputInt implements Function<String, Integer> {

    public final static String NOT_CORRECT_INT = "Число введено некорректно.";

    @Override
    public Integer apply(String anyInt) {
        try {
            return Integer.valueOf(anyInt);
        } catch (Exception e) {
            throw new RuntimeException(NOT_CORRECT_INT);
        }
    }
}
