package ru.bank.consultant.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.context.Context;
import ru.bank.consultant.entity.Bank;
import ru.bank.consultant.repository.BankRepository;
import ru.bank.consultant.service.PdfService;

import java.util.Arrays;
import java.util.List;

@Controller
public class PdfController {

    private final PdfService pdfService;
    private final BankRepository bankRepository;

    public PdfController(PdfService pdfService, BankRepository bankRepository) {
        this.pdfService = pdfService;
        this.bankRepository = bankRepository;
    }

    @PostMapping("/generate-pdf")
    public ResponseEntity<byte[]> generatePdf(
            @RequestParam(required = false) Long bankId,
            @RequestParam(required = false, defaultValue = "consumer") String creditType
    ) {
        try {
            Context context = new Context();
            
            // Получаем банк
            if (bankId != null) {
                Bank bank = bankRepository.findById(bankId).orElse(null);
                if (bank != null) {
                    context.setVariable("bankName", bank.getName());
                }
            }
            
            // Настройка документов в зависимости от типа кредита
            String creditTypeName;
            List<String> documents;
            List<String> optionalDocuments;

            switch (creditType) {
                case "mortgage":
                    creditTypeName = "Ипотека";
                    documents = Arrays.asList(
                            "Паспорт гражданина РФ",
                            "СНИЛС",
                            "ИНН",
                            "Военный билет (для мужчин до 27 лет)",
                            "Копия трудовой книжки, заверенная работодателем",
                            "Справка о доходах (2-НДФЛ или по форме банка)",
                            "Свидетельство о браке/разводе (при наличии)"
                    );
                    optionalDocuments = Arrays.asList(
                            "Документы на имеющуюся недвижимость или транспорт (для подтверждения платежеспособности)",
                            "Свидетельство о рождении детей"
                    );
                    break;
                case "car":
                    creditTypeName = "Автокредит";
                    documents = Arrays.asList(
                            "Паспорт гражданина РФ",
                            "Водительское удостоверение",
                            "Справка о доходах (2-НДФЛ)"
                    );
                    optionalDocuments = Arrays.asList(
                            "Заверенная копия трудовой книжки",
                            "СНИЛС или ИНН"
                    );
                    break;
                case "card":
                    creditTypeName = "Кредитная карта";
                    documents = Arrays.asList(
                            "Паспорт гражданина РФ"
                    );
                    optionalDocuments = Arrays.asList(
                            "Заграничный паспорт со штампами о выездах",
                            "Свідетельство о регистрации ТС (автомобиль на вас)"
                    );
                    break;
                case "consumer":
                default:
                    creditTypeName = "Потребительский кредит";
                    documents = Arrays.asList(
                            "Паспорт гражданина РФ",
                            "СНИЛС",
                            "Справка 2-НДФЛ за последние 6 месяцев"
                    );
                    optionalDocuments = Arrays.asList(
                            "Копия трудовой книжки (заверенная)",
                            "Выписка по зарплатному счету"
                    );
                    break;
            }

            context.setVariable("creditTypeName", creditTypeName);
            context.setVariable("documents", documents);
            context.setVariable("optionalDocuments", optionalDocuments);

            // Генерация PDF
            byte[] pdfBytes = pdfService.generatePdfFromHtml("pdf-docs", context);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "bank_documents.pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);

        } catch (Exception e) {
            System.err.println("Ошибка при генерации PDF: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
