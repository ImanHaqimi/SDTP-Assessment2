package com.photogallery.service;

import com.photogallery.model.photo;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class photoartist {
    private final WebClient webClient;

    public photoartist(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://web.socem.plymouth.ac.uk").build();
    }

    public List<photo> getPhotosByArtist(int memberId) {
        Mono<photo[]> response = webClient.get()
                .uri("/COMP2005/photographic-gallery-webservice/Photo")
                .retrieve()
                .bodyToMono(photo[].class);

        return Arrays.stream(response.block())
                .filter(photo -> photo.getMemberID() == memberId)
                .collect(Collectors.toList());
    }
}
