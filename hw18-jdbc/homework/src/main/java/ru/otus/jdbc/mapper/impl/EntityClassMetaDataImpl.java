package ru.otus.jdbc.mapper.impl;

import ru.otus.jdbc.mapper.EntityClassMetaData;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {

    private final Class<T> type;
    private final Field idField;
    private final Constructor<T> constructor;
    private final List<Field> fields;

    public EntityClassMetaDataImpl(Class<T> type, Field idField, Constructor<T> constructor, List<Field> fields) {
        this.type = type;
        this.idField = idField;
        this.constructor = constructor;
        this.fields = fields;
    }

    @Override
    public String getName() {
        return type.getSimpleName();
    }

    @Override
    public Constructor<T> getConstructor() {
        return constructor;
    }

    @Override
    public Field getIdField() {
        return idField;
    }

    @Override
    public List<Field> getAllFields() {
        return fields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return fields.stream()
                .filter(field -> !field.equals(idField))
                .collect(Collectors.toList());
    }
}
