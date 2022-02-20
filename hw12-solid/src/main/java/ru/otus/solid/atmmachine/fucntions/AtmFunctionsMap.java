package ru.otus.solid.atmmachine.fucntions;

import java.util.Map;
import java.util.stream.Collectors;

public class AtmFunctionsMap<T, R extends AtmFunction> implements AtmFunctions<T, R> {

    public final static String NOT_SUPPORT_FUNCTION = "Банкомат не поддерживает данную функцию.";

    private final Map<T, R> atmFunctionMap;

    public AtmFunctionsMap(Map<T, R> atmFunctionMap) {
        this.atmFunctionMap = atmFunctionMap;
    }

    @Override
    public R getFunction(T functionId) {
        if (!atmFunctionMap.containsKey(functionId)) {
            throw new RuntimeException(NOT_SUPPORT_FUNCTION);
        }
        return atmFunctionMap.get(functionId);
    }

    @Override
    public String toString() {
        return atmFunctionMap.entrySet().stream()
                .map(entry -> String.format("%s. %s", entry.getKey(), entry.getValue()))
                .collect(Collectors.joining("\n"));
    }
}