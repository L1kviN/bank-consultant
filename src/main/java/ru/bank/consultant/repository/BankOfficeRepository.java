package ru.bank.consultant.repository;

import ru.bank.consultant.entity.BankOffice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BankOfficeRepository extends JpaRepository<BankOffice, Long> {

    List<BankOffice> findByBankId(Long bankId);

    List<BankOffice> findByBankIdOrderByAddressAsc(Long bankId);

    @Query("SELECT o FROM BankOffice o WHERE o.lat IS NULL OR o.lng IS NULL")
    List<BankOffice> findByLatIsNullOrLngIsNull();

    @Query("SELECT o FROM BankOffice o WHERE o.bank.id = :bankId AND (o.lat IS NULL OR o.lng IS NULL)")
    List<BankOffice> findByBankIdAndMissingCoordinates(@Param("bankId") Long bankId);

    @Query("SELECT COUNT(o) FROM BankOffice o WHERE o.lat IS NOT NULL AND o.lng IS NOT NULL")
    long countByLatIsNotNullAndLngIsNotNull();

    @Query("SELECT COUNT(o) FROM BankOffice o WHERE o.lat IS NULL OR o.lng IS NULL")
    long countByLatIsNullOrLngIsNull();
}