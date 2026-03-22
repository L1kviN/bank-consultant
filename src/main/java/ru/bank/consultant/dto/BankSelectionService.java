package ru.bank.consultant.service;

import ru.bank.consultant.dto.BankOfferDto;
import ru.bank.consultant.dto.CreditFilterDto;
import ru.bank.consultant.entity.Bank;
import ru.bank.consultant.entity.CreditOffer;
import ru.bank.consultant.repository.BankCreditTypeRepository;
import ru.bank.consultant.repository.BankRepository;
import ru.bank.consultant.repository.CreditOfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class BankSelectionService {

    @Autowired
    private BankRepository bankRepository;

    @Autowired
    private CreditOfferRepository creditOfferRepository;

    @Autowired
    private BankCreditTypeRepository bankCreditTypeRepository;

    @Autowired
    private CalculatorService calculatorService;

    public List<BankOfferDto> findSuitableBanks(CreditFilterDto filter) {
        List<BankOfferDto> result = new ArrayList<>();

        // 1. Находим банки, подходящие по базовым параметрам
        List<Bank> suitableBanks = bankRepository.findSuitableBanks(
                filter.getIncome(),
                filter.getAmount(),
                filter.getTermMonths()
        );

        // 2. Фильтруем по типу кредита
        for (Bank bank : suitableBanks) {
            boolean hasCreditType = bankCreditTypeRepository
                    .existsByBankIdAndCreditType(bank.getId(), filter.getCreditType());

            if (!hasCreditType) continue;

            // 3. Получаем условия кредитования для этого банка и типа кредита
            CreditOffer offer = creditOfferRepository
                    .findByBankIdAndCreditType(bank.getId(), filter.getCreditType())
                    .orElse(null);

            if (offer == null) continue;

            // 4. Рассчитываем ежемесячный платеж (берем среднюю ставку)
            BigDecimal avgRate = offer.getMinRate().add(offer.getMaxRate())
                    .divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP);

            BigDecimal monthlyPayment = calculatorService.calculateAnnuityPayment(
                    filter.getAmount(),
                    filter.getTermMonths(),
                    avgRate
            );

            // 5. Создаем DTO для вывода
            BankOfferDto dto = new BankOfferDto();
            dto.setBankId(bank.getId());
            dto.setBankName(bank.getName());
            dto.setLogoUrl(bank.getLogoUrl());
            dto.setCreditType(filter.getCreditType());
            dto.setMinRate(offer.getMinRate());
            dto.setMaxRate(offer.getMaxRate());
            dto.setMonthlyPayment(monthlyPayment);
            dto.setMaxAmount(bank.getMaxAmount());
            dto.setMinIncome(bank.getMinIncome());

            result.add(dto);
        }

        return result;
    }
}