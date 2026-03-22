package ru.bank.consultant.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CreditFilterDto {
    private String creditType;
    private BigDecimal amount;
    private Integer termMonths;
    private BigDecimal income;
    private String employmentType;
    private String creditHistory;
}