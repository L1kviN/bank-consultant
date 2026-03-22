package ru.bank.consultant.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StartController {

    @GetMapping("/start")
    public String start() {
        // проверяем, авторизован ли пользователь
        boolean isAuthenticated = SecurityContextHolder.getContext().getAuthentication() != null &&
                SecurityContextHolder.getContext().getAuthentication().isAuthenticated() &&
                !SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser");

        if (isAuthenticated) {
            // если авторизован, сразу на подбор
            return "redirect:/dashboard";
        } else {
            // если не авторизован, на страницу выбора: вход или регистрация
            return "redirect:/choose-action";
        }
    }

    @GetMapping("/choose-action")
    public String chooseAction() {
        return "choose-action";
    }
}