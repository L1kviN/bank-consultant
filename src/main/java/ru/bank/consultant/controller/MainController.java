package ru.bank.consultant.controller;

import ru.bank.consultant.dto.RegistrationDto;
import ru.bank.consultant.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class MainController {

    @Autowired
    private AuthService authService;

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/contacts")
    public String contacts() {
        return "contacts";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/login-success")
    public String loginSuccess() {
        return "redirect:/dashboard";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute RegistrationDto dto, RedirectAttributes redirectAttributes) {
        System.out.println("=== КОНТРОЛЛЕР ===");
        System.out.println("Email: " + dto.getEmail());

        boolean success = authService.registerUser(
                dto.getFullName(),
                dto.getEmail(),
                dto.getPassword(),
                dto.getPhone(),
                dto.getBirthDate()
        );

        if (success) {
            redirectAttributes.addFlashAttribute("success", "Регистрация успешна! Войдите в систему.");
            return "redirect:/login";
        } else {
            redirectAttributes.addFlashAttribute("error", "Пользователь с таким email уже существует");
            return "redirect:/register";
        }
    }


}