package ru.bank.consultant.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Entity
@Table(name = "credit_offers")
public class CreditOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bank_id", nullable = false)
    private Bank bank;

    @Column(name = "credit_type", nullable = false)
    private String creditType;

    @Column(name = "min_rate", nullable = false)
    private BigDecimal minRate;

    @Column(name = "max_rate", nullable = false)
    private BigDecimal maxRate;

    @Column(name = "required_documents", columnDefinition = "JSON")
    private String requiredDocuments;
}