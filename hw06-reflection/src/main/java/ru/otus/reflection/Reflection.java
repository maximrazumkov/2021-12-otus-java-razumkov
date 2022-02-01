/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package ru.otus.reflection;

import ru.otus.reflection.annotations.After;
import ru.otus.reflection.annotations.Before;
import ru.otus.reflection.annotations.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

public class Reflection {

    private final static String NOT_ARGS = "Необходимо передать название класса для тестирования.";
    private final static String NOT_CORRECT_SET_ANNOTATION = "Не может метод теста быть помечен несколькими аннтациями.";
    private final static String SHOULD_NOT_BE_ANY_BEFORE = "Тестовый класс не может иметь несколько аннотоций @before";
    private final static String SHOULD_NOT_BE_ANY_AFTER = "Тестовый класс не может иметь несколько аннотоций @after";
    private final static String COULD_NOT_CALL_AFTER = "Не удалось вызвать метод after";
    private final static String STATISTIC_HEADER = "Результат выполнения тесто:\n";
    private final static String TEST_RESULT = "%s - %s";
    private final static String COUNT_OK = "Успешно - %d";
    private final static String COUNT_FAIL = "Не успешно - %d";
    private final static String COUNT_TEST = "Всего - %d";

    public static void main(String... args) throws Exception {
        checkArgs(args);
        final TestMetaInfo testMetaInfo = initTestMetaInfo(args[0]);
        execute(testMetaInfo);
        printStatistic(testMetaInfo);
    }

    private static void printStatistic(TestMetaInfo testMetaInfo) {
        System.out.println();
        System.out.println(STATISTIC_HEADER);
        testMetaInfo.getResult().entrySet().forEach(
                entry -> System.out.println(String.format(TEST_RESULT, entry.getKey(), entry.getValue()))
        );
        System.out.println();
        System.out.println(String.format(COUNT_OK, testMetaInfo.getCountOk()));
        System.out.println(String.format(COUNT_FAIL, testMetaInfo.getCountFail()));
        System.out.println(String.format(COUNT_TEST, testMetaInfo.getCountOk() + testMetaInfo.getCountFail()));
    }

    private final static void checkArgs(String... args) {
        if (args.length == 0) {
            throw new RuntimeException(NOT_ARGS);
        }
    }

    private static TestMetaInfo initTestMetaInfo(String className) throws Exception {
        TestMetaInfo metaInfo = new TestMetaInfo();
        Class<?> clazz = Class.forName(className);
        metaInfo.setType(clazz);
        Method[] methods = clazz.getDeclaredMethods();
        Arrays.stream(methods).forEach(method -> {
            Before before = method.getDeclaredAnnotation(Before.class);
            After after = method.getDeclaredAnnotation(After.class);
            Test test = method.getDeclaredAnnotation(Test.class);
            checkAnnotation(metaInfo, method, before, after, test);
            if (before != null) {
                metaInfo.setBefore(method);
            } else if (after != null) {
                metaInfo.setAfter(method);
            } else if (test != null) {
                metaInfo.getMethods().add(method);
            }
        });
        return metaInfo;
    }

    private static void checkAnnotation(TestMetaInfo metaInfo, Method method, Before before, After after, Test test) {
        if (method.getDeclaredAnnotations().length > 1) {
            throw new RuntimeException(NOT_CORRECT_SET_ANNOTATION);
        }
        if (before != null && metaInfo.getBefore() != null) {
            throw new RuntimeException(SHOULD_NOT_BE_ANY_BEFORE);
        }
        if (after != null && metaInfo.getAfter() != null) {
            throw new RuntimeException(SHOULD_NOT_BE_ANY_AFTER);
        }
    }

    private static void execute(TestMetaInfo metaInfo) {
        Method before = metaInfo.getBefore();
        Method after = metaInfo.getBefore();
        Class<?> type = metaInfo.getType();
        Map<String, State> result = metaInfo.getResult();
        metaInfo.getMethods().stream().forEach(method -> {
            Object instance = null;
            try {
                instance = ReflectionHelper.instantiate(type);
                if (before.getName() != null) {
                    ReflectionHelper.callMethod(instance, before.getName());
                }
                ReflectionHelper.callMethod(instance, method.getName());
                ReflectionHelper.callMethod(instance, after.getName());
                result.put(method.getName(), State.OK);
                metaInfo.setCountOk(metaInfo.getCountOk() + 1);
            } catch (Exception e) {
                result.put(method.getName(), State.FAIL);
                metaInfo.setCountFail(metaInfo.getCountFail() + 1);
            } finally {
                try {
                    ReflectionHelper.callMethod(instance, after.getName());
                } catch (Exception e) {
                    System.out.println(COULD_NOT_CALL_AFTER);
                }
            }
        });
    }
}
