package ru.bank.consultant.controller;

import ru.bank.consultant.entity.Bank;
import ru.bank.consultant.entity.BankOffice;
import ru.bank.consultant.repository.BankOfficeRepository;
import ru.bank.consultant.repository.BankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MapController {

    @Autowired
    private BankOfficeRepository bankOfficeRepository;

    @Autowired
    private BankRepository bankRepository;

    @GetMapping("/map")
    public String showMap(@RequestParam Long bankId,
                          @RequestParam(required = false) Double lat,
                          @RequestParam(required = false) Double lng,
                          Model model) {
        Bank bank = bankRepository.findById(bankId).orElse(null);
        List<BankOffice> offices = bankOfficeRepository.findByBankId(bankId);

        List<Map<String, Object>> officesData = new ArrayList<>();
        for (BankOffice office : offices) {
            Map<String, Object> officeData = new HashMap<>();
            officeData.put("id", office.getId());
            officeData.put("address", office.getAddress());
            officeData.put("lat", office.getLat());
            officeData.put("lng", office.getLng());
            officeData.put("workTime", office.getWorkTime());
            officeData.put("phone", office.getPhone());
            officesData.add(officeData);
        }

        model.addAttribute("bankName", bank != null ? bank.getName() : "Банк");
        model.addAttribute("officesData", officesData);
        model.addAttribute("userLat", lat);
        model.addAttribute("userLng", lng);

        return "map";
    }
}