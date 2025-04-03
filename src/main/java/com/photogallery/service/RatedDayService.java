package com.photogallery.service;

import com.photogallery.model.Rating;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RatedDayService {
    private final WebClient webClient;

    public RatedDayService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://web.socem.plymouth.ac.uk").build();
    }

    public DayOfWeek getMostRatedDay() {
        Rating[] ratings = webClient.get()
                .uri("/COMP2005/photographic-gallery-webservice/Rating")
                .retrieve()
                .bodyToMono(Rating[].class)
                .block();

        if (ratings == null) return null;

        Map<DayOfWeek, Long> countByDay = Arrays.stream(ratings)
                .collect(Collectors.groupingBy(
                        rating -> rating.getRatingDate().getDayOfWeek(),
                        Collectors.counting()
                ));

        return Collections.max(countByDay.entrySet(), Map.Entry.comparingByValue()).getKey();
    }
}
