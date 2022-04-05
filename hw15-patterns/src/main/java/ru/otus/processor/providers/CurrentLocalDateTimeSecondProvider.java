package ru.otus.processor.providers;

import java.time.LocalDateTime;

public class CurrentLocalDateTimeSecondProvider implements SecondProvider {
    @Override
    public LocalDateTime getCurrentSecond() {
        return LocalDateTime.now();
    }
}
