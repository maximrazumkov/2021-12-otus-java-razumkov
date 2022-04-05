package ru.otus.processor.providers;

import java.time.LocalDateTime;

public class CurrentLocalDateTimeSecondProvider implements SecondProvider {
    @Override
    public int getCurrentSecond() {
        return LocalDateTime.now().getSecond();
    }
}
