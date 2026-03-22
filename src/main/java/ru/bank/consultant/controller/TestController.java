package ru.bank.consultant.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
public class TestController {

    @GetMapping("/test")
    public String test() {
        return "Сервер работает!";
    }

    @PostMapping("/test-register")
    public Map<String, String> testRegister(
            @RequestParam String fullName,
            @RequestParam String email,
            @RequestParam String password) {

        Map<String, String> result = new HashMap<>();
        result.put("status", "получено");
        result.put("fullName", fullName);
        result.put("email", email);
        result.put("password", password);
        return result;
    }
}