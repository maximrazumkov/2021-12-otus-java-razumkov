package ru.otus.reflection;

import ru.otus.reflection.annotations.After;
import ru.otus.reflection.annotations.Before;
import ru.otus.reflection.annotations.Test;

import java.util.HashMap;
import java.util.UUID;

public class DailyServiceTest {

    private DailyService dailyService;

    @Before
    public void initTest() {
        dailyService = new DailyServiceImpl(new HashMap<>());
    }

    @Test
    public void whenAddTaskThenTaskAdded() {
        dailyService.addTask("new task");
    }

    @Test
    public void whenReadAllTasksThenGetTasksList() {
        dailyService.addTask("new task 1");
        dailyService.addTask("new task 2");
        dailyService.addTask("new task 3");
        dailyService.readAllTasks();
    }

    @Test
    public void whenFindTaskByUuidThenGetTask() {
        UUID uuid = dailyService.addTask("new task");
        dailyService.findTaskByUuid(uuid);
    }

    @Test
    public void whenFindTaskByNullThrowException() {
        dailyService.findTaskByUuid(null);
    }

    @Test
    public void whenAddNullThenThrowException() {
        dailyService.addTask(null);
    }

    @After
    public void finishTest() {
        dailyService = null;
    }
}
