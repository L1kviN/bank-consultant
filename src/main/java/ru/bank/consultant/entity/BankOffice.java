package ru.bank.consultant.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

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

    @Column(name = "lat", precision = 10, scale = 8)
    private BigDecimal lat;

    @Column(name = "lng", precision = 11, scale = 8)
    private BigDecimal lng;

    @Column(name = "work_time")
    private String workTime;

    @Column(name = "phone")
    private String phone;

    public BankOffice() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Bank getBank() { return bank; }
    public void setBank(Bank bank) { this.bank = bank; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public BigDecimal getLat() { return lat; }
    public void setLat(BigDecimal lat) { this.lat = lat; }

    public BigDecimal getLng() { return lng; }
    public void setLng(BigDecimal lng) { this.lng = lng; }

    public String getWorkTime() { return workTime; }
    public void setWorkTime(String workTime) { this.workTime = workTime; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}