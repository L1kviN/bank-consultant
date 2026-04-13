package ru.bank.consultant.service;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;
import java.io.File;

@Service
public class PdfService {

    private final TemplateEngine templateEngine;

    public PdfService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public byte[] generatePdfFromHtml(String templateName, Context context) throws Exception {
        // Рендер HTML из Thymeleaf шаблона
        String htmlContent = templateEngine.process(templateName, context);

        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            
            // Подключаем системный шрифт Arial для поддержки кириллицы (Windows)
            File fontFile = new File("C:/Windows/Fonts/arial.ttf");
            if (fontFile.exists()) {
                builder.useFont(fontFile, "Arial");
            }
            
            builder.withHtmlContent(htmlContent, "http://localhost:8080/");
            builder.toStream(os);
            builder.run();
            return os.toByteArray();
        }
    }
}
