package com.photogallery.service;

import com.photogallery.model.Member;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


import java.util.Arrays;
import java.util.List;

@Service
public class MemberService {
    private final WebClient webClient;

    // Inject WebClient.Builder instead of instantiating it
    public MemberService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://web.socem.plymouth.ac.uk").build();
    }
    //https://web.socem.plymouth.ac.uk/COMP2005/photographic-gallery-webservice/Member

    public List<Member> fetchMembers() {
        Mono<Member[]> response = webClient.get()
                .uri("/COMP2005/photographic-gallery-webservice/Member")
                .retrieve()
                .bodyToMono(Member[].class);

        return Arrays.asList(response.block()); // Convert to List
    }
}

