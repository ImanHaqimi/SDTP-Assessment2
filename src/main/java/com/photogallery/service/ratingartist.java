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
public class ratingartist {
    private final WebClient webClient;

    public ratingartist(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://web.socem.plymouth.ac.uk").build();
    }

    public double getAverageRatingByArtist(int artistId) {
        Mono<photo[]> photosResponse = webClient.get()
                .uri("/COMP2005/photographic-gallery-webservice/Photo")
                .retrieve()
                .bodyToMono(photo[].class);

        Mono<rating[]> ratingsResponse = webClient.get()
                .uri("/COMP2005/photographic-gallery-webservice/Rating")
                .retrieve()
                .bodyToMono(rating[].class);

        List<Integer> artistPhotoIds = Arrays.stream(photosResponse.block()) // Fetch artist's photos
                .filter(photo -> photo.getMemberID() == artistId)
                .map(photo::getId)
                .collect(Collectors.toList());

        List<Integer> ratings = Arrays.stream(ratingsResponse.block()) // Fetch ratings
                .filter(rating -> artistPhotoIds.contains(rating.getPhotoID())) // Filter artist's photos
                .map(rating::getRatingValue)
                .collect(Collectors.toList());

        return ratings.stream().mapToInt(Integer::intValue).average().orElse(0.0); // Compute average
    }
}

