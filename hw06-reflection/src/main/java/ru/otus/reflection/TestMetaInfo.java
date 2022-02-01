package ru.otus.reflection;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class TestMetaInfo {
    private Class<?> type;
    private Method before;
    private Method after;
    private final List<Method> methods = new ArrayList<>();
    private final Map<String, State> result = new HashMap<>();
    private int countOk;
    private int countFail;
}
