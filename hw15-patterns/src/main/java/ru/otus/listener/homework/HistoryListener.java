package ru.otus.listener.homework;

import ru.otus.listener.Listener;
import ru.otus.model.Message;

import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

public class HistoryListener implements Listener, HistoryReader {

    private final Set<Message> setMessages;

    public HistoryListener(Set<Message> setMessages) {
        this.setMessages = setMessages;
    }

    @Override
    public void onUpdated(Message msg) {
        setMessages.add(msg.toBuilder().build());
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        return setMessages.stream()
                .filter(message -> message.getId() == id)
                .map(message -> message.toBuilder().build())
                .findFirst();
    }
}
