package ru.otus.dataprocessor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.otus.model.Measurement;
import ru.otus.model.MixMeasurement;

import java.io.InputStream;
import java.util.List;

public class ResourcesFileLoader implements Loader {

    private final String fileName;
    private final ObjectMapper objectMapper = new ObjectMapper()
            .addMixIn(Measurement.class, MixMeasurement.class);

    public ResourcesFileLoader(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<Measurement> load() {
        //читает файл, парсит и возвращает результат
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream(fileName);
            return objectMapper.readValue(is, new TypeReference<>(){});
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
