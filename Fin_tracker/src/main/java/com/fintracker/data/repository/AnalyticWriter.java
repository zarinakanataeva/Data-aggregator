package com.fintracker.data.repository;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fintracker.data.model.Analytic;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Класс, записывающий аналитику в файл.
 */
public class AnalyticWriter implements AnalyticRepository {
    private final Path filePath;
    private final ObjectMapper mapper;

    public AnalyticWriter(String filePath) {
        this.filePath = Path.of(filePath);
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Override
    public void save(Analytic analytic) {
        if (analytic == null) throw new IllegalArgumentException("Аналитика не может быть null");
        try {
            Path parent = filePath.getParent();
            if (parent != null) Files.createDirectories(parent);

            ArrayNode history = mapper.createArrayNode();
            if (Files.exists(filePath) && Files.size(filePath) > 0) {
                JsonNode existing = mapper.readTree(filePath.toFile());
                if (existing.isArray()) history.addAll((ArrayNode) existing);
                else if (!existing.isNull()) history.add(existing);
            }
            history.add(mapper.valueToTree(analytic));
            mapper.writeValue(filePath.toFile(), history);
        } catch (IOException exception) {
            throw new IllegalStateException("Не удалось сохранить аналитику в " + filePath, exception);
        }
    }
}
