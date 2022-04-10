package ru.otus.mapper;

public interface InitializerEntityClassMetaData {
    <T> EntityClassMetaData<T> init(Class<T> clazz);
}
