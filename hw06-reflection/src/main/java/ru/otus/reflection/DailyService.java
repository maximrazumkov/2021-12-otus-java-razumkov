package ru.otus.reflection;

import java.util.Map;
import java.util.UUID;

public interface DailyService {
    UUID addTask(String textTask);
    Map<UUID, String> readAllTasks();
    String findTaskByUuid(UUID uuid);
}
