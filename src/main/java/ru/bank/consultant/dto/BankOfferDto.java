package ru.bank.consultant.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class BankOfferDto {
    private Long bankId;
    private String bankName;
    private String logoUrl;
    private String creditType;
    private BigDecimal minRate;
    private BigDecimal maxRate;
    private BigDecimal monthlyPayment;
    private BigDecimal maxAmount;
    private BigDecimal minIncome;
}