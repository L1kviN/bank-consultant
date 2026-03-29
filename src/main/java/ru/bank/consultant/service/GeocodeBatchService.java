package ru.bank.consultant.service;

import ru.bank.consultant.entity.BankOffice;
import ru.bank.consultant.repository.BankOfficeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class GeocodeBatchService {

    private final BankOfficeRepository bankOfficeRepository;
    private final DaDataService daDataService;

    @Autowired
    public GeocodeBatchService(BankOfficeRepository bankOfficeRepository, DaDataService daDataService) {
        this.bankOfficeRepository = bankOfficeRepository;
        this.daDataService = daDataService;
    }

    public BatchResult updateAllMissingCoordinates() {
        List<BankOffice> offices = bankOfficeRepository.findByLatIsNullOrLngIsNull();

        if (offices.isEmpty()) {
            return new BatchResult(0, 0, "Нет отделений без координат");
        }

        System.out.println("Начинаем геокодирование " + offices.size() + " отделений...");

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);

        Flux.fromIterable(offices)
                .flatMap(officeItem -> {
                    String address = officeItem.getAddress();
                    if (address == null || address.isEmpty()) {
                        failCount.incrementAndGet();
                        return Mono.empty();
                    }
                    return daDataService.geocodeAddress(address)
                            .flatMap(coords -> {
                                officeItem.setLat(coords.getLat());
                                officeItem.setLng(coords.getLon());
                                bankOfficeRepository.save(officeItem);
                                successCount.incrementAndGet();
                                System.out.println("✅ Геокодировано: " + address);
                                return Mono.just(officeItem);
                            })
                            .onErrorResume(e -> {
                                failCount.incrementAndGet();
                                System.err.println("❌ Ошибка: " + address + " - " + e.getMessage());
                                return Mono.empty();
                            });
                }, 5)
                .blockLast();

        System.out.println("Геокодирование завершено. Успешно: " + successCount.get() + ", Ошибок: " + failCount.get());

        return new BatchResult(successCount.get(), failCount.get(),
                String.format("Обновлено %d из %d", successCount.get(), offices.size()));
    }

    public BatchResult updateBankCoordinates(Long bankId) {
        List<BankOffice> offices = bankOfficeRepository.findByBankIdAndMissingCoordinates(bankId);

        if (offices.isEmpty()) {
            return new BatchResult(0, 0, "Нет отделений без координат для этого банка");
        }

        System.out.println("Начинаем геокодирование " + offices.size() + " отделений для банка ID=" + bankId);

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);

        Flux.fromIterable(offices)
                .flatMap(officeItem -> {
                    String address = officeItem.getAddress();
                    if (address == null || address.isEmpty()) {
                        failCount.incrementAndGet();
                        return Mono.empty();
                    }
                    return daDataService.geocodeAddress(address)
                            .flatMap(coords -> {
                                officeItem.setLat(coords.getLat());
                                officeItem.setLng(coords.getLon());
                                bankOfficeRepository.save(officeItem);
                                successCount.incrementAndGet();
                                return Mono.just(officeItem);
                            })
                            .onErrorResume(e -> {
                                failCount.incrementAndGet();
                                return Mono.empty();
                            });
                }, 5)
                .blockLast();

        return new BatchResult(successCount.get(), failCount.get(),
                String.format("Обновлено %d из %d для банка ID=%d", successCount.get(), offices.size(), bankId));
    }

    public CoordinatesStats getCoordinatesStats() {
        long withCoords = bankOfficeRepository.countByLatIsNotNullAndLngIsNotNull();
        long withoutCoords = bankOfficeRepository.countByLatIsNullOrLngIsNull();
        long total = withCoords + withoutCoords;

        return new CoordinatesStats(total, withCoords, withoutCoords);
    }

    public static class BatchResult {
        public int success;
        public int failed;
        public String message;

        public BatchResult(int success, int failed, String message) {
            this.success = success;
            this.failed = failed;
            this.message = message;
        }
    }

    public static class CoordinatesStats {
        public long total;
        public long withCoordinates;
        public long withoutCoordinates;

        public CoordinatesStats(long total, long withCoordinates, long withoutCoordinates) {
            this.total = total;
            this.withCoordinates = withCoordinates;
            this.withoutCoordinates = withoutCoordinates;
        }
    }
}