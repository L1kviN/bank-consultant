package ru.bank.consultant.service;

import ru.bank.consultant.entity.User;
import ru.bank.consultant.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean registerUser(String fullName, String email, String password, String phone, LocalDate birthDate) {
        if (userRepository.existsByEmail(email)) {
            return false;
        }

        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setPhone(phone);
        user.setBirthDate(birthDate);
        user.setIsActive(true);

        userRepository.save(user);
        return true;
    }
}