package ru.otus.model;

import java.util.ArrayList;
import java.util.List;

public class ObjectForMessage implements Cloneable {
    private List<String> data;

    public ObjectForMessage() {
    }

    public ObjectForMessage(ObjectForMessage objectForMessage) {
        this.data = new ArrayList<>(objectForMessage.data);
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ObjectForMessage{" +
                "data=" + data +
                '}';
    }
}
