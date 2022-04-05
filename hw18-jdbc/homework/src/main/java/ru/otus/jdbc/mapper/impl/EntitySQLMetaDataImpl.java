package ru.otus.jdbc.mapper.impl;

import ru.otus.jdbc.mapper.EntityClassMetaData;
import ru.otus.jdbc.mapper.EntitySQLMetaData;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {

    private static final String SELECT = "SELECT %s from %s";
    private static final String SELECT_BY_ID = "SELECT %s from %s WHERE %s = ?";
    private static final String INSERT = "INSERT INTO %s(%s) VALUES (%s)";
    private static final String UPDATE = "UPDATE %s SET %s WHERE %s = ?";

    private final EntityClassMetaData<?> entityClassMetaDataClient;

    public EntitySQLMetaDataImpl(EntityClassMetaData<?> entityClassMetaDataClient) {
        this.entityClassMetaDataClient = entityClassMetaDataClient;
    }

    @Override
    public String getSelectAllSql() {
        return String.format(SELECT, getAllFields(Field::getName), getTableName());
    }

    @Override
    public String getSelectByIdSql() {
        return String.format(SELECT_BY_ID, getAllFields(Field::getName), getTableName(), getIdFieldName());
    }

    @Override
    public String getInsertSql() {
        return String.format(INSERT, getTableName(), getFieldsWithoutId(Field::getName), getFieldsWithoutId(field -> "?"));
    }

    @Override
    public String getUpdateSql() {
        return String.format(UPDATE, getTableName(), getFieldsWithoutId(field -> field.getName() + " = ?"), getIdFieldName());
    }

    private String getAllFields(Function<Field, String> mapper) {
        return getFields(entityClassMetaDataClient.getAllFields(), mapper);
    }

    private String getTableName() {
        return entityClassMetaDataClient.getName().toLowerCase(Locale.ROOT);
    }

    private String getIdFieldName() {
        return entityClassMetaDataClient.getIdField().getName();
    }

    private String getFieldsWithoutId(Function<Field, String> mapper) {
        return getFields(entityClassMetaDataClient.getFieldsWithoutId(), mapper);
    }

    private String getFields(List<Field> fields, Function<Field, String> mapper) {
        return fields.stream()
                .map(mapper)
                .collect(Collectors.joining(", "));
    }
}
