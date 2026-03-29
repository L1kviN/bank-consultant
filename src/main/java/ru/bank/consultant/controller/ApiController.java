package ru.bank.consultant.controller;

import ru.bank.consultant.dto.dadata.SuggestionResponse;
import ru.bank.consultant.service.DaDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private DaDataService daDataService;

    @GetMapping("/search-banks")
    public Mono<SuggestionResponse> searchBanks(
            @RequestParam String query,
            @RequestParam(required = false) String city) {
        return daDataService.searchBanks(query, city);
    }

    @GetMapping("/detect-city")
    public Mono<String> detectCity(@RequestParam String ip) {
        return daDataService.detectCityByIp(ip);
    }

    @GetMapping("/user-role")
    public Mono<String> getUserRole() {
        String role = SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().stream()
                .findFirst()
                .map(auth -> auth.getAuthority().replace("ROLE_", ""))
                .orElse("user");
        return Mono.just(role);
    }
}