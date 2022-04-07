package ru.otus.jdbc.mapper;

import java.sql.ResultSet;
import java.util.List;

public interface EntitySqlParams<T> {
    T getObject(ResultSet rs);
    List<Object> getFiledValues(T obj);
    List<Object> getFiledValuesWithoutId(T obj);
}
