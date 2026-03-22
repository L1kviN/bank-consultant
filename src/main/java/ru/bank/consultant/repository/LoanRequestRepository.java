package ru.bank.consultant.repository;

import ru.bank.consultant.entity.LoanRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LoanRequestRepository extends JpaRepository<LoanRequest, Long> {
    List<LoanRequest> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<LoanRequest> findByStatus(String status);
}