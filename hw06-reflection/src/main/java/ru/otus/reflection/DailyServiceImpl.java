package ru.otus.reflection;

import java.util.Map;
import java.util.UUID;

public class DailyServiceImpl implements DailyService {

    private final Map<UUID, String> tasks;

    public DailyServiceImpl(Map<UUID, String> tasks) {
        this.tasks = tasks;
    }

    @Override
    public UUID addTask(String textTask) {
        if (textTask == null || textTask.equals("")) {
            throw new RuntimeException("textTask must have anytext");
        }
        UUID uuid = UUID.randomUUID();
        tasks.put(uuid, textTask);
        return uuid;
    }

    @Override
    public Map<UUID, String> readAllTasks() {
        return tasks;
    }

    @Override
    public String findTaskByUuid(UUID uuid) {
        if (uuid == null) {
            throw new RuntimeException("uuid mustn't be null");
        }
        return tasks.get(uuid);
    }

}
