package ru.bank.consultant.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "loan_requests")
public class LoanRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "term_months", nullable = false)
    private Integer termMonths;

    @Column(name = "purpose")
    private String purpose;

    @Column(name = "income", nullable = false)
    private BigDecimal income;

    @Column(name = "employment_type")
    private String employmentType;

    @Column(name = "credit_history")
    private String creditHistory;

    @Column(name = "monthly_payment")
    private BigDecimal monthlyPayment;

    @ManyToOne
    @JoinColumn(name = "selected_bank_id")
    private Bank selectedBank;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RequestStatus status = RequestStatus.in_progress;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}