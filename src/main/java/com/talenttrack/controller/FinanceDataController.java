package com.talenttrack.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.talenttrack.service.FinanceDataService;

import reactor.core.publisher.Mono;
@RestController
public class FinanceDataController {

    @Autowired
    private FinanceDataService financeDataService;

    @GetMapping("/finance/realtime-quotes")
    public Mono<ResponseEntity<String>> getRealtimeQuotes(@RequestParam String saIds) {
        return financeDataService.getRealtimeQuotes(saIds)
                .map(ResponseEntity::ok) // Wrap the response in ResponseEntity
                .onErrorResume(e -> {
                    // Log the error for debugging
                    e.printStackTrace();
                    if (e instanceof WebClientResponseException) {
                        WebClientResponseException webClientResponseException = (WebClientResponseException) e;
                        if (webClientResponseException.getStatusCode() == HttpStatus.FORBIDDEN) {
                            return Mono.just(ResponseEntity.status(HttpStatus.FORBIDDEN)
                                    .body("Access forbidden. Check your API key and permissions."));
                        }
                    }
                    return Mono.just(ResponseEntity.internalServerError()
                            .body("Error fetching data: " + e.getMessage()));
                });
    }

}
