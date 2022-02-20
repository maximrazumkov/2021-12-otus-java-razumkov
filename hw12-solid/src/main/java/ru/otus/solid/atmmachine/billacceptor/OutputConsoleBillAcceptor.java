package ru.otus.solid.atmmachine.billacceptor;

import ru.otus.solid.atmmachine.io.Output;
import ru.otus.solid.atmmachine.models.Money;

import java.util.function.Consumer;

public class OutputConsoleBillAcceptor implements Consumer<Money> {

    public final static String GAVE_MONEY = "Выданно:";

    private final Output output;

    public OutputConsoleBillAcceptor(Output output) {
        this.output = output;
    }

    @Override
    public void accept(Money money) {
        output.print(GAVE_MONEY);
        output.print(money.toString());
    }
}
