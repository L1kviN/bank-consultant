package ru.bank.consultant.controller;

import ru.bank.consultant.entity.LoanRequest;
import ru.bank.consultant.service.LoanRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HistoryController {

    @Autowired
    private LoanRequestService loanRequestService;

    @GetMapping("/profile/history")
    public String history(Model model) {
        List<LoanRequest> requests = loanRequestService.getUserRequests();
        model.addAttribute("requests", requests);
        return "history";
    }
}