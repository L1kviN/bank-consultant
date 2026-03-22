package ru.bank.consultant.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Entity
@Table(name = "bank_offices")
public class BankOffice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bank_id", nullable = false)
    private Bank bank;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "lat", nullable = false)
    private BigDecimal lat;

    @Column(name = "lng", nullable = false)
    private BigDecimal lng;

    @Column(name = "work_time")
    private String workTime;

    @Column(name = "phone")
    private String phone;
}