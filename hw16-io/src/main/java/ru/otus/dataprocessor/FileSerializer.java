package ru.otus.dataprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.otus.model.Measurement;

import java.io.PrintWriter;
import java.util.Map;

public class FileSerializer implements Serializer {

    private final String fileName;
    private final ObjectMapper objectMapper = new ObjectMapper()
            .addMixIn(Measurement.class, MixMeasurement.class);

    public FileSerializer(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void serialize(Map<String, Double> data) {
        try(PrintWriter out = new PrintWriter(fileName)) {
            out.print(objectMapper.writeValueAsString(data));
        } catch (Exception e) {
            throw new FileProcessException(e);
        }
    }
}

