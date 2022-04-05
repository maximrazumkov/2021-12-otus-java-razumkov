package ru.otus.processor.providers;

import java.time.LocalDateTime;

@FunctionalInterface
public interface SecondProvider {
    LocalDateTime getCurrentSecond();
}
