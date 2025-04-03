package com.photogallery.service;

import com.photogallery.model.Photo;
import com.photogallery.model.Rating;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Arrays;

@Service
public class RatingArtistService {
    private final WebClient webClient;

    public RatingArtistService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://web.socem.plymouth.ac.uk").build();
    }

    public double getAverageRatingByArtist(int artistId) {
        Photo[] photos = webClient.get()
                .uri("/COMP2005/photographic-gallery-webservice/Photo")
                .retrieve()
                .bodyToMono(Photo[].class)
                .block();

        Rating[] ratings = webClient.get()
                .uri("/COMP2005/photographic-gallery-webservice/Rating")
                .retrieve()
                .bodyToMono(Rating[].class)
                .block();

        if (photos == null || ratings == null) return 0.0;

        List<Integer> artistPhotoIds = Arrays.stream(photos)
                .filter(photo -> photo.getMemberID() == artistId)
                .map(Photo::getId)
                .collect(Collectors.toList());

        return Arrays.stream(ratings)
                .filter(rating -> artistPhotoIds.contains(rating.getPhotoID()))
                .mapToInt(Rating::getRatingValue)
                .average()
                .orElse(0.0);
    }
}
