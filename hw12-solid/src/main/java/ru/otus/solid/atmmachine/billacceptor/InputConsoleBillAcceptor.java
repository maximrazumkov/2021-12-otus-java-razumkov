package ru.otus.solid.atmmachine.billacceptor;

import ru.otus.solid.atmmachine.io.Input;
import java.util.function.Function;
import java.util.function.Supplier;

public class InputConsoleBillAcceptor implements Supplier<Integer> {

    private final Function<String, Integer> convertStringToInt;
    private final Input input;

    public InputConsoleBillAcceptor(Function<String, Integer> convertStringToInt, Input input) {
        this.convertStringToInt = convertStringToInt;
        this.input = input;
    }

    @Override
    public Integer get() {
        return convertStringToInt.apply(input.readLine());
    }
}
