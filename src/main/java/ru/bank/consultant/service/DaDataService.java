package ru.bank.consultant.service;

import ru.bank.consultant.dto.dadata.IpLocationResponse;
import ru.bank.consultant.dto.dadata.SuggestionResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.math.BigDecimal;

@Service
public class DaDataService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public DaDataService(@Value("${dadata.api.key}") String apiKey) {
        this.objectMapper = new ObjectMapper();
        this.webClient = WebClient.builder()
                .baseUrl("https://suggestions.dadata.ru/suggestions/api/4_1/rs")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("Authorization", "Token " + apiKey)
                .build();
    }

    public Mono<SuggestionResponse> searchBanks(String query, String city) {
        String requestBody = city != null && !city.isEmpty()
                ? String.format("{\"query\":\"%s\",\"locations\":[{\"city\":\"%s\"}],\"count\":10}", query, city)
                : String.format("{\"query\":\"%s\",\"count\":10}", query);

        return webClient.post()
                .uri("/suggest/bank")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(SuggestionResponse.class);
    }

    public Mono<String> detectCityByIp(String ip) {
        String requestBody = String.format("{\"ip\":\"%s\"}", ip);

        return webClient.post()
                .uri("/iplocate/address")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(IpLocationResponse.class)
                .map(response -> {
                    if (response.getLocation() != null &&
                            response.getLocation().getData() != null &&
                            response.getLocation().getData().getCity() != null) {
                        return response.getLocation().getData().getCity();
                    }
                    if (response.getData() != null && response.getData().getCity() != null) {
                        return response.getData().getCity();
                    }
                    return "москва";
                });
    }

    public Mono<Coordinates> geocodeAddress(String address) {
        String requestBody = String.format("{\"query\":\"%s\"}", address);

        return webClient.post()
                .uri("/clean/address")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(response -> {
                    try {
                        JsonNode root = objectMapper.readTree(response);
                        if (root.isArray() && root.size() > 0) {
                            JsonNode firstResult = root.get(0);
                            if (firstResult != null) {
                                String lat = firstResult.has("geo_lat") ? firstResult.get("geo_lat").asText() : null;
                                String lon = firstResult.has("geo_lon") ? firstResult.get("geo_lon").asText() : null;
                                if (lat != null && lon != null && !lat.isEmpty() && !lon.isEmpty() &&
                                        !lat.equals("0") && !lon.equals("0")) {
                                    return Mono.just(new Coordinates(new BigDecimal(lat), new BigDecimal(lon)));
                                }
                            }
                        }
                        return Mono.error(new RuntimeException("Не удалось получить координаты для: " + address));
                    } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
                        return Mono.error(new RuntimeException("Ошибка геокодирования: " + address, e));
                    }
                });
    }

    public Mono<Boolean> checkConnection() {
        return webClient.post()
                .uri("/suggest/bank")
                .bodyValue("{\"query\":\"сбер\"}")
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> true)
                .onErrorReturn(false);
    }

    public static class Coordinates {
        private BigDecimal lat;
        private BigDecimal lon;

        public Coordinates(BigDecimal lat, BigDecimal lon) {
            this.lat = lat;
            this.lon = lon;
        }

        public BigDecimal getLat() { return lat; }
        public void setLat(BigDecimal lat) { this.lat = lat; }
        public BigDecimal getLon() { return lon; }
        public void setLon(BigDecimal lon) { this.lon = lon; }
    }
}