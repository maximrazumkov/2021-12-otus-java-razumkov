package ru.otus.processor;

import ru.otus.model.Message;
import ru.otus.processor.providers.SecondProvider;

public class ThrowerExceptionEvenSecondProcessor implements Processor {

    private final Processor processor;
    private final SecondProvider secondProvider;

    public ThrowerExceptionEvenSecondProcessor(Processor processor, SecondProvider secondProvider) {
        this.processor = processor;
        this.secondProvider = secondProvider;
    }

    @Override
    public Message process(Message message) {
        if ((secondProvider.getCurrentSecond() % 2) == 0) {
            throw new RuntimeException();
        }
        return processor.process(message);
    }
}
