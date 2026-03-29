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

    public String getCreditType() { return creditType; }
    public void setCreditType(String creditType) { this.creditType = creditType; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public Integer getTermMonths() { return termMonths; }
    public void setTermMonths(Integer termMonths) { this.termMonths = termMonths; }
    public BigDecimal getIncome() { return income; }
    public void setIncome(BigDecimal income) { this.income = income; }
    public String getEmploymentType() { return employmentType; }
    public void setEmploymentType(String employmentType) { this.employmentType = employmentType; }
    public String getCreditHistory() { return creditHistory; }
    public void setCreditHistory(String creditHistory) { this.creditHistory = creditHistory; }

}