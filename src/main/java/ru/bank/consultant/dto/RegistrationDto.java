package ru.bank.consultant.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class RegistrationDto {
    private String fullName;
    private String email;
    private String password;
    private String phone;
    private LocalDate birthDate;
}