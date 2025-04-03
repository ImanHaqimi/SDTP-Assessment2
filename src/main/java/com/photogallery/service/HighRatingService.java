package com.photogallery.service;

import com.photogallery.model.Photo;
import com.photogallery.model.Rating;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Arrays;

@Service
public class HighRatingService {
    private static final Logger logger = LoggerFactory.getLogger(HighRatingService.class);
    private final WebClient webClient;

    public HighRatingService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://web.socem.plymouth.ac.uk").build();
    }

    public List<Photo> getHighRatedPhotos() {
        try {
            Rating[] ratings = webClient.get()
                    .uri("/COMP2005/photographic-gallery-webservice/Rating")
                    .retrieve()
                    .bodyToMono(Rating[].class)
                    .block();

            if (ratings == null) return List.of();

            List<Integer> highRatedPhotoIds = Arrays.stream(ratings)
                    .filter(rating -> rating.getRatingValue() > 4)
                    .map(Rating::getPhotoID)
                    .distinct()
                    .collect(Collectors.toList());

            Photo[] photos = webClient.get()
                    .uri("/COMP2005/photographic-gallery-webservice/Photo")
                    .retrieve()
                    .bodyToMono(Photo[].class)
                    .block();

            if (photos == null) return List.of();

            return Arrays.stream(photos)
                    .filter(photo -> highRatedPhotoIds.contains(photo.getId()))
                    .collect(Collectors.toList());

        } catch (Exception e) {
            logger.error("Error fetching high-rated photos", e);
            return List.of();
        }
    }
}
