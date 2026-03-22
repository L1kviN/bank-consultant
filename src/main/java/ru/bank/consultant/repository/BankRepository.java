package ru.bank.consultant.repository;

import ru.bank.consultant.entity.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.util.List;

public interface BankRepository extends JpaRepository<Bank, Long> {

    @Query("SELECT b FROM Bank b WHERE b.minIncome <= :income AND b.maxAmount >= :amount AND b.minTerm <= :term AND b.maxTerm >= :term")
    List<Bank> findSuitableBanks(@Param("income") BigDecimal income,
                                 @Param("amount") BigDecimal amount,
                                 @Param("term") Integer term);
}