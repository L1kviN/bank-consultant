package ru.bank.consultant.repository;

import ru.bank.consultant.entity.BankOffice;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BankOfficeRepository extends JpaRepository<BankOffice, Long> {
    List<BankOffice> findByBankId(Long bankId);
}