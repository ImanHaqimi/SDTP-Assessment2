package com.photogallery.service;

import com.photogallery.model.photo;
import com.photogallery.model.rating;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Arrays;

@Service
public class highrating {
    private final WebClient webClient;

    public highrating (WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://web.socem.plymouth.ac.uk").build();
    }

    public List<photo> getHighRatedPhotos() {
        Mono<rating[]> ratingsResponse = webClient.get()
                .uri("/COMP2005/photographic-gallery-webservice/Rating")
                .retrieve()
                .bodyToMono(rating[].class);

        Mono<photo[]> photosResponse = webClient.get()
                .uri("/COMP2005/photographic-gallery-webservice/Photo")
                .retrieve()
                .bodyToMono(photo[].class);

        List<Integer> highRatedPhotoIds = Arrays.stream(ratingsResponse.block()) // Fetch ratings
                .filter(rating -> rating.getRatingValue() > 4) // Filter ratings above 4
                .map(rating::getPhotoID) // Extract photo IDs
                .distinct() // Avoid duplicates
                .collect(Collectors.toList());

        return Arrays.stream(photosResponse.block()) // Fetch photos
                .filter(photo -> highRatedPhotoIds.contains(photo.getId())) // Match high-rated IDs
                .collect(Collectors.toList());
    }
}
