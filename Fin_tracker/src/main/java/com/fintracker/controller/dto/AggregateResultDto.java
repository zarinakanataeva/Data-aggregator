package com.fintracker.controller.dto;

import lombok.Data;
import java.math.BigDecimal;

/**
 * DTO для передачи результатов агрегирования транзакций.
 */

@Data
public class AggregateResultDto {
    private BigDecimal sum = BigDecimal.ZERO;
    private BigDecimal average = BigDecimal.ZERO;
    private long count;
}
