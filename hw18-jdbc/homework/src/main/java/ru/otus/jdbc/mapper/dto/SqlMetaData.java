package ru.otus.jdbc.mapper.dto;

import lombok.Getter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Getter
public class SqlMetaData<T> {
    private Class<T> type;
    private Field idField;
    private Constructor<T> constructor;
    private List<Field> fields = new ArrayList<>();

    private SqlMetaData() {}

    private SqlMetaData(Class<T> type, Field idField, Constructor<T> constructor, List<Field> fields) {
        this.type = type;
        this.idField = idField;
        this.constructor = constructor;
        this.fields = fields;
    }

    public static SqlMetaDataBuilder builder() {
        return new SqlMetaDataBuilder<>();
    }

    public static class SqlMetaDataBuilder<T> {
        private Class<T> type;
        private Field idField;
        private Constructor<T> constructor;
        private List<Field> fields;

        private SqlMetaDataBuilder() {

        }

        public SqlMetaDataBuilder setType(Class<T> type) {
            this.type = type;
            return this;
        }

        public SqlMetaDataBuilder setIdField(Field idField) {
            this.idField = idField;
            return this;
        }

        public SqlMetaDataBuilder setConstructor(Constructor<T> constructor) {
            this.constructor = constructor;
            return this;
        }

        public SqlMetaDataBuilder setFields(List<Field> fields) {
            this.fields = fields;
            return this;
        }

        public SqlMetaData<T> build() {
            return new SqlMetaData<>(type, idField, constructor, fields);
        }
    }
}
