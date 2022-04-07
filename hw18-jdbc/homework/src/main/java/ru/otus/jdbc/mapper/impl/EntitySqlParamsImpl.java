package ru.otus.jdbc.mapper.impl;

import ru.otus.jdbc.mapper.EntityClassMetaData;
import ru.otus.jdbc.mapper.EntitySqlParams;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EntitySqlParamsImpl<T> implements EntitySqlParams<T> {

    private final EntityClassMetaData<?> entityClassMetaDataClient;

    public EntitySqlParamsImpl(EntityClassMetaData<?> entityClassMetaDataClient) {
        this.entityClassMetaDataClient = entityClassMetaDataClient;
    }

    @Override
    public T getObject(ResultSet rs) {
        try {
            List<Object> objs = new ArrayList<>();
            for (Field field : entityClassMetaDataClient.getAllFields()) {
                objs.add(rs.getObject(field.getName()));
            }
            return (T) entityClassMetaDataClient.getConstructor().newInstance(objs.toArray());
        } catch (Exception e) {
            throw new RuntimeException("Не удалось обработать ResultSet", e);
        }
    }

    @Override
    public List<Object> getFiledValues(T obj) {
        List<Object> fieldValues = getFiledValuesWithoutId(obj);
        fieldValues.add(getFieldValue(obj, entityClassMetaDataClient.getIdField().getName()));
        return fieldValues;
    }

    @Override
    public List<Object> getFiledValuesWithoutId(T obj) {
        return entityClassMetaDataClient.getFieldsWithoutId().stream()
                .map(field -> getFieldValue(obj, field.getName()))
                .collect(Collectors.toList());
    }

    private Object getFieldValue(T object, String name) {
        try {
            var field = object.getClass().getDeclaredField(name);
            field.setAccessible(true);
            return field.get(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
