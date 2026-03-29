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

    public Long getBankId() { return bankId; }
    public void setBankId(Long bankId) { this.bankId = bankId; }
    public String getBankName() { return bankName; }
    public void setBankName(String bankName) { this.bankName = bankName; }
    public String getLogoUrl() { return logoUrl; }
    public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }
    public String getCreditType() { return creditType; }
    public void setCreditType(String creditType) { this.creditType = creditType; }
    public BigDecimal getMinRate() { return minRate; }
    public void setMinRate(BigDecimal minRate) { this.minRate = minRate; }
    public BigDecimal getMaxRate() { return maxRate; }
    public void setMaxRate(BigDecimal maxRate) { this.maxRate = maxRate; }
    public BigDecimal getMonthlyPayment() { return monthlyPayment; }
    public void setMonthlyPayment(BigDecimal monthlyPayment) { this.monthlyPayment = monthlyPayment; }
    public BigDecimal getMaxAmount() { return maxAmount; }
    public void setMaxAmount(BigDecimal maxAmount) { this.maxAmount = maxAmount; }
    public BigDecimal getMinIncome() { return minIncome; }
    public void setMinIncome(BigDecimal minIncome) { this.minIncome = minIncome; }
}