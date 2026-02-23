package com.fintracker.data.repository;


import com.fintracker.data.model.Analytic;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * Класс, записывающий аналитику в файл.
 */
public class AnalyticWriter implements AnalyticRepository {

    private String filePath;
    private final ObjectMapper mapper;

    public AnalyticWriter(String filePath) {
        this.filePath = filePath;
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Override
    public void save(Analytic analytic) {
        try {
            mapper.writeValue(new File(filePath), analytic);
            System.out.println("Аналитика успешно сохранена в файл: " + filePath);
        } catch (IOException e) {
            System.err.println("Ошибка при сохранении аналитики: " + e.getMessage());
        }
    }
}
