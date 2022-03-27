package ru.otus.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class MixMeasurement {
    @JsonCreator
    MixMeasurement(@JsonProperty("name") String name, @JsonProperty("value") double value) { }
}
