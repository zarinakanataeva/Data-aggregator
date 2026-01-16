package com.skillbox.data.model;

import com.skillbox.controller.dto.AggregateResultDto;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Класс, хранящий результаты расчета аналитики транзакций
 */

public class Analytic {
private String description;

private final Map<String, AggregateResultDto> results;

public Analytic (String description, Map<String, AggregateResultDto> results){
    this.description = description;
    this.results = results;
}

public String getDescription(){
    return description;
}
    public Map<String, AggregateResultDto> getResults() { return results; }

    @Override
    public String toString(){

    StringBuilder sb = new StringBuilder();
        sb.append("--- Результаты аналитики: ").append(description).append(" ---\n");
        results.forEach((key, value) -> sb.append(String.format("%s: %s\n", key, value)));
        return sb.toString();

    }

}
