package ru.otus.mapper.impl;

import ru.otus.mapper.EntityClassMetaData;
import ru.otus.mapper.InitializerEntityClassMetaData;
import ru.otus.mapper.annotation.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class InitializerEntityClassMetaDataImpl implements InitializerEntityClassMetaData {

    @Override
    public <T> EntityClassMetaData<T> init(Class<T> clazz) {
        try {
            return new EntityClassMetaDataImpl<T>(
                    clazz,
                    getIdField(clazz.getDeclaredFields()),
                    getConstructor(clazz),
                    getFields(clazz.getDeclaredFields())
            );
        } catch (Exception e) {
            throw new RuntimeException("Не удалось обработать Entity", e);
        }
    }

    private <T> Constructor<T> getConstructor(Class<T> clazz) throws Exception {
        return clazz.getConstructor(getConstructParameters(clazz));
    }

    private Class<?>[] getConstructParameters(Class<?> clazz) {
        return getFields(clazz.getDeclaredFields()).stream()
                .map(Field::getType)
                .toArray(Class<?>[]::new);
    }

    private Field getIdField(Field[] fields) {
        return Arrays.stream(fields).filter(field -> field.getDeclaredAnnotation(Id.class) != null)
                .findFirst().orElseThrow(RuntimeException::new);
    }

    private List<Field> getFields(Field[] fields) {
        return Arrays.stream(fields).toList();
    }
}
