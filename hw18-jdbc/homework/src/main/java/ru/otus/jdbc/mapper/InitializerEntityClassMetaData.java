package ru.otus.jdbc.mapper;

public interface InitializerEntityClassMetaData {
    <T> EntityClassMetaData<T> init(Class<T> clazz);
}
