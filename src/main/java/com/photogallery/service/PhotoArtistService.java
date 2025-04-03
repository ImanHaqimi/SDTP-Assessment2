package com.photogallery.service;

import com.photogallery.model.Photo;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PhotoArtistService {
    private final WebClient webClient;

    public PhotoArtistService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://web.socem.plymouth.ac.uk").build();
    }

    public List<Photo> getPhotosByArtist(int memberId) {
        Photo[] photos = webClient.get()
                .uri("/COMP2005/photographic-gallery-webservice/Photo")
                .retrieve()
                .bodyToMono(Photo[].class)
                .block();

        if (photos == null) return List.of();

        return Arrays.stream(photos)
                .filter(photo -> photo.getMemberID() == memberId)
                .collect(Collectors.toList());
    }
}
