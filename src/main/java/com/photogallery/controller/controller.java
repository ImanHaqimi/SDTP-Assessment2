package com.photogallery.controller;

import com.photogallery.service.highrating;
import com.photogallery.service.photoartist;
import com.photogallery.service.ratedday;
import com.photogallery.service.ratingartist;
import com.photogallery.model.photo;  // Fixed class name to PascalCase
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.DayOfWeek;
import java.util.List;

@RestController
@RequestMapping("/api")
public class controller {  // Fixed class name to PascalCase
    private final highrating highratingService;
    private final ratingartist ratingartistService;
    private final ratedday rateddayService;
    private final photoartist photoartistService;

    public controller(highrating highratingService,
                      ratingartist ratingartistService,
                      ratedday rateddayService,
                      photoartist photoartistService) {
        this.highratingService = highratingService;
        this.ratingartistService = ratingartistService;
        this.rateddayService = rateddayService;
        this.photoartistService = photoartistService;
    }

    // F1: Get photos by a specific artist (memberID)
    @GetMapping("/artist/{artistId}")
    public List<photo> getPhotosByArtist(@PathVariable int artistId) {
        return photoartistService.getPhotosByArtist(artistId);
    }

    // F2: Get all photos rated above 4 stars
    @GetMapping("/highrated")
    public List<photo> getHighlyRatedPhotos() {
        return highratingService.getHighRatedPhotos();
    }

    // F3: Find the most common rating day
    @GetMapping("/day")
    public DayOfWeek getMostCommonRatingDay() {
        return rateddayService.getMostRatedDay();
    }

    // F4: Get the average rating of an artist’s photos
    @GetMapping("/average/{artistId}")
    public double getAverageRatingByArtist(@PathVariable int artistId) {
        return ratingartistService.getAverageRatingByArtist(artistId);
    }
}
