package ru.bank.consultant.controller;

import ru.bank.consultant.dto.RegistrationDto;
import ru.bank.consultant.service.AuthService;
import ru.bank.consultant.security.JwtUtil;
import ru.bank.consultant.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class MainController {

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

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

    @PostMapping("/login")
    public String loginProcess(@RequestParam String username,
                               @RequestParam String password,
                               HttpServletResponse response,
                               RedirectAttributes redirectAttributes) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            String token = jwtUtil.generateToken(userDetails);

            Cookie cookie = new Cookie("JWT_TOKEN", token);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge((int) (jwtUtil.getJwtExpiration() / 1000));
            
            response.addCookie(cookie);
            return "redirect:/dashboard";
        } catch (org.springframework.security.core.AuthenticationException e) {
            redirectAttributes.addFlashAttribute("error", "Неверный email или пароль");
            return "redirect:/login";
        }
    }

    @GetMapping("/register")
    public String register() {
        return "register";
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