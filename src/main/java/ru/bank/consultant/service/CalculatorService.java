package ru.bank.consultant.service;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class CalculatorService {

    /**
     * Расчет аннуитетного платежа
     * @param amount сумма кредита
     * @param months срок в месяцах
     * @param annualRate годовая процентная ставка
     * @return ежемесячный платеж
     */
    public BigDecimal calculateAnnuityPayment(BigDecimal amount, int months, BigDecimal annualRate) {
        if (annualRate.compareTo(BigDecimal.ZERO) == 0) {
            return amount.divide(BigDecimal.valueOf(months), 2, RoundingMode.HALF_UP);
        }

        // месячная ставка = годовая / 12 / 100
        BigDecimal monthlyRate = annualRate
                .divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP)
                .divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);

        // (1 + ставка)^срок
        BigDecimal factor = BigDecimal.ONE.add(monthlyRate).pow(months);

        // платеж = сумма * (ставка * фактор) / (фактор - 1)
        BigDecimal numerator = monthlyRate.multiply(factor);
        BigDecimal denominator = factor.subtract(BigDecimal.ONE);
        BigDecimal annuity = numerator.divide(denominator, 10, RoundingMode.HALF_UP);

        return amount.multiply(annuity).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Расчет кредитной нагрузки (процент от дохода)
     * @param monthlyPayment ежемесячный платеж
     * @param monthlyIncome ежемесячный доход
     * @return процент нагрузки
     */
    public BigDecimal calculateDebtLoad(BigDecimal monthlyPayment, BigDecimal monthlyIncome) {
        if (monthlyIncome.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.valueOf(100);
        }
        return monthlyPayment
                .divide(monthlyIncome, 2, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }
}