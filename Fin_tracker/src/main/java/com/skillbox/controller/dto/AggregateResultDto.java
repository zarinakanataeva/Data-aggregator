package com.skillbox.controller.dto;

import lombok.Data;

/**
 * DTO для передачи результатов агрегирования транзакций.
 */

@Data
public class AggregateResultDto {
    private double sum;
    private double average;
    private long count;
    
}