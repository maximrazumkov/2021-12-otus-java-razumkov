package ru.otus.processor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import ru.otus.model.Message;
import ru.otus.processor.providers.SecondProvider;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ThrowerExceptionEvenSecondProcessorTest {

    private Processor throwerExceptionEvenSecondProcessor;
    private SecondProvider secondProvider;
    private Processor anyProcessor;

    @BeforeEach
    public void initThrowerExceptionEvenSecondProcessor() {
        anyProcessor = BDDMockito.mock(Processor.class);
        secondProvider = BDDMockito.mock(SecondProvider.class);
        throwerExceptionEvenSecondProcessor = new ThrowerExceptionEvenSecondProcessor(anyProcessor, secondProvider);
    }

    @Test
    @DisplayName("Должен выбросить исключение в четную секунду")
    public void shouldThrowExceptionWhenEvanSecond() {
        Message message = BDDMockito.mock(Message.class);
        LocalDateTime localDateTime = LocalDateTime.of(2022,12,12,12,12, 12);
        BDDMockito.given(secondProvider.getCurrentSecond()).willReturn(localDateTime);
        assertThrows(RuntimeException.class, () -> throwerExceptionEvenSecondProcessor.process(message));
        verify(anyProcessor, times(0)).process(message);
    }

    @Test
    @DisplayName("Должен продолжить работу в нечетную секунду")
    public void shouldWorkSuccessWhenOddSecond() {
        Message message = BDDMockito.mock(Message.class);
        LocalDateTime localDateTime = LocalDateTime.of(2022,12,12,12,13, 13);
        BDDMockito.given(secondProvider.getCurrentSecond()).willReturn(localDateTime);
        throwerExceptionEvenSecondProcessor.process(message);
        verify(anyProcessor, times(1)).process(message);
    }
}
