package ru.bank.consultant.repository;

import ru.bank.consultant.entity.BankCreditType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BankCreditTypeRepository extends JpaRepository<BankCreditType, Long> {
    List<BankCreditType> findByBankId(Long bankId);
    boolean existsByBankIdAndCreditType(Long bankId, String creditType);
}