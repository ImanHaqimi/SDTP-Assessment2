package com.photogallery.service;

import com.photogallery.model.rating;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.Arrays;

@Service
public class ratedday {
    private final WebClient webClient;

    public ratedday (WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://web.socem.plymouth.ac.uk").build();
    }

    public DayOfWeek getMostRatedDay() {
        Mono<rating[]> response = webClient.get()
                .uri("/COMP2005/photographic-gallery-webservice/Rating")
                .retrieve()
                .bodyToMono(rating[].class);

        Map<DayOfWeek, Long> countByDay = Arrays.stream(response.block()) // Fetch ratings
                .collect(Collectors.groupingBy(
                        rating -> rating.getRatingDate().getDayOfWeek(), // Extract the day of the week
                        Collectors.counting()
                ));


        return Collections.max(countByDay.entrySet(), Map.Entry.comparingByValue()).getKey(); // Get most common day
    }
}
