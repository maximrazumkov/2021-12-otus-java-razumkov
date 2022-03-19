package ru.otus.listener.homework;

import ru.otus.listener.Listener;
import ru.otus.model.Message;

import java.util.Map;
import java.util.Optional;

public class HistoryListener implements Listener, HistoryReader {

    private final Map<Long, Message> messagesMap;

    public HistoryListener(Map<Long, Message> setMessages) {
        this.messagesMap = setMessages;
    }

    @Override
    public void onUpdated(Message msg) {
        messagesMap.put(msg.getId(), msg.clone());
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        Message message = messagesMap.get(id);
        return message == null ? Optional.empty() : Optional.of(message.clone());
    }
}
