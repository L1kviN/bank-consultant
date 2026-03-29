package ru.bank.consultant.controller;

import ru.bank.consultant.dto.dadata.SuggestionResponse;
import ru.bank.consultant.service.DaDataService;
import ru.bank.consultant.service.GeocodeBatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private DaDataService daDataService;

    @Autowired
    private GeocodeBatchService geocodeBatchService;

    @GetMapping("/banks")
    public String banksPanel(Model model) {
        GeocodeBatchService.CoordinatesStats stats = geocodeBatchService.getCoordinatesStats();
        model.addAttribute("totalOffices", stats.total);
        model.addAttribute("withCoords", stats.withCoordinates);
        model.addAttribute("withoutCoords", stats.withoutCoordinates);
        model.addAttribute("searchQuery", "");
        return "admin-banks";
    }

    @PostMapping("/search-banks-api")
    @ResponseBody
    public Mono<SuggestionResponse> searchBanksApi(@RequestParam String query, @RequestParam String city) {
        return daDataService.searchBanks(query, city);
    }

    @PostMapping("/update-coordinates")
    @ResponseBody
    public GeocodeBatchService.BatchResult updateCoordinates() {
        return geocodeBatchService.updateAllMissingCoordinates();
    }

    @PostMapping("/update-bank-coordinates")
    @ResponseBody
    public GeocodeBatchService.BatchResult updateBankCoordinates(@RequestParam Long bankId) {
        return geocodeBatchService.updateBankCoordinates(bankId);
    }

    @GetMapping("/coordinates-stats")
    @ResponseBody
    public GeocodeBatchService.CoordinatesStats getCoordinatesStats() {
        return geocodeBatchService.getCoordinatesStats();
    }
}