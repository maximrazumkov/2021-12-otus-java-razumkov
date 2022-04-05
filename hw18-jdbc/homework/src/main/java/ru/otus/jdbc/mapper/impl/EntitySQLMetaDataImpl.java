package ru.otus.jdbc.mapper.impl;

import ru.otus.jdbc.mapper.EntityClassMetaData;
import ru.otus.jdbc.mapper.EntitySQLMetaData;

import java.lang.reflect.Field;
import java.util.Locale;
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
        return String.format(SELECT, getAllFields(), getTableName());
    }

    @Override
    public String getSelectByIdSql() {
        return String.format(SELECT_BY_ID, getAllFields(), getTableName(), getIdFieldName());
    }

    @Override
    public String getInsertSql() {
        return String.format(INSERT, getTableName(), getAllFields(), getValues());
    }

    @Override
    public String getUpdateSql() {
        return String.format(UPDATE, getTableName(), getSetUpdate(), getIdFieldName());
    }

    private String getAllFields() {
        return entityClassMetaDataClient.getAllFields().stream()
                .map(Field::getName)
                .collect(Collectors.joining(", "));
    }

    private String getTableName() {
        return entityClassMetaDataClient.getName().toLowerCase(Locale.ROOT);
    }

    private String getIdFieldName() {
        return entityClassMetaDataClient.getIdField().getName();
    }

    private String getValues() {
        return entityClassMetaDataClient.getAllFields().stream()
                .map(field -> "?")
                .collect(Collectors.joining(", "));
    }

    private String getSetUpdate() {
        return entityClassMetaDataClient.getAllFields().stream()
                .map(field -> field.getName() + " = ?")
                .collect(Collectors.joining(", "));
    }
}
