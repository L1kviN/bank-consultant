package ru.bank.consultant.controller;

import ru.bank.consultant.dto.BankOfferDto;
import ru.bank.consultant.dto.CreditFilterDto;
import ru.bank.consultant.entity.LoanRequest;
import ru.bank.consultant.service.BankSelectionService;
import ru.bank.consultant.service.CalculatorService;
import ru.bank.consultant.service.LoanRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

@Controller
public class CreditController {

    @Autowired
    private BankSelectionService bankSelectionService;

    @Autowired
    private CalculatorService calculatorService;

    @Autowired
    private LoanRequestService loanRequestService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("filter", new CreditFilterDto());
        return "dashboard";
    }

    @PostMapping("/find-banks")
    public String findBanks(@ModelAttribute CreditFilterDto filter, Model model) {
        BigDecimal avgRate = BigDecimal.valueOf(15.0);
        BigDecimal monthlyPayment = calculatorService.calculateAnnuityPayment(
                filter.getAmount(),
                filter.getTermMonths(),
                avgRate
        );

        LoanRequest savedRequest = loanRequestService.saveRequestAndReturn(filter, monthlyPayment);

        List<BankOfferDto> offers = bankSelectionService.findSuitableBanks(filter);
        model.addAttribute("offers", offers);
        model.addAttribute("filter", filter);

        if (savedRequest != null) {
            model.addAttribute("requestId", savedRequest.getId());
        }

        return "results";
    }

    @PostMapping("/documents-from-history")
    public String documentsFromHistory(@RequestParam Long bankId, @RequestParam String creditType, Model model) {
        model.addAttribute("bankId", bankId);
        model.addAttribute("creditType", creditType);
        return "documents";
    }

    @PostMapping("/map-from-history")
    public String mapFromHistory(@RequestParam Long bankId, Model model) {
        model.addAttribute("bankId", bankId);
        return "redirect:/map?bankId=" + bankId;
    }

    @PostMapping("/select-bank")
    public String selectBank(@RequestParam Long bankId,
                             @RequestParam String creditType,
                             @RequestParam(required = false) Long requestId,
                             @RequestParam(required = false) Double userLat,
                             @RequestParam(required = false) Double userLng,
                             Model model) {
        if (requestId != null) {
            loanRequestService.updateSelectedBank(requestId, bankId);
        }

        model.addAttribute("bankId", bankId);
        model.addAttribute("creditType", creditType);

        if (userLat != null && userLng != null) {
            model.addAttribute("userLat", userLat);
            model.addAttribute("userLng", userLng);
        }

        return "documents";
    }
}