package ru.otus.jdbc.mapper.impl;

import ru.otus.jdbc.mapper.EntityClassMetaData;
import ru.otus.jdbc.mapper.annotation.Id;
import ru.otus.jdbc.mapper.dto.SqlMetaData;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {

    private SqlMetaData<T> sqlMetaData;

    private EntityClassMetaDataImpl(SqlMetaData<T> sqlMetaData) {
        this.sqlMetaData = sqlMetaData;
    }

    public static <T> EntityClassMetaData<T> initEntityClassMetaData(Class<T> clazz) {
        return new EntityClassMetaDataImpl<>(getSqlMetaData(clazz));
    }

    private static <T> SqlMetaData<T> getSqlMetaData(Class<T> clazz) {
        try {
            return SqlMetaData.builder()
                    .setType(clazz)
                    .setConstructor(clazz.getConstructor())
                    .setIdField(getIdField(clazz.getDeclaredFields()))
                    .setFields(getFields(clazz.getDeclaredFields()))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Не удалось обработать Entity", e);
        }
    }

    private static Field getIdField(Field[] fields) {
        return Arrays.stream(fields).filter(field -> field.getDeclaredAnnotation(Id.class) != null)
                .findFirst().orElseThrow(RuntimeException::new);
    }

    private static List<Field> getFields(Field[] fields) {
        return Arrays.stream(fields).toList();
    }

    @Override
    public String getName() {
        return sqlMetaData.getType().getSimpleName();
    }

    @Override
    public Constructor<T> getConstructor() {
        return sqlMetaData.getConstructor();
    }

    @Override
    public Field getIdField() {
        return sqlMetaData.getIdField();
    }

    @Override
    public List<Field> getAllFields() {
        return sqlMetaData.getFields();
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return sqlMetaData.getFields().stream()
                .filter(field -> !field.equals(sqlMetaData.getIdField()))
                .collect(Collectors.toList());
    }
}
