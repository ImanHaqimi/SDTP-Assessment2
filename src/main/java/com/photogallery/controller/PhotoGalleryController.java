package com.photogallery.controller;

import com.photogallery.service.HighRatingService;
import com.photogallery.service.PhotoArtistService;
import com.photogallery.service.RatedDayService;
import com.photogallery.service.RatingArtistService;
import com.photogallery.model.Photo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")  // Allow frontend access
public class PhotoGalleryController {
    private static final Logger logger = LoggerFactory.getLogger(PhotoGalleryController.class);

    private final HighRatingService highRatingService;
    private final RatingArtistService ratingArtistService;
    private final RatedDayService ratedDayService;
    private final PhotoArtistService photoArtistService;

    public PhotoGalleryController(HighRatingService highRatingService,
                                  RatingArtistService ratingArtistService,
                                  RatedDayService ratedDayService,
                                  PhotoArtistService photoArtistService) {
        this.highRatingService = highRatingService;
        this.ratingArtistService = ratingArtistService;
        this.ratedDayService = ratedDayService;
        this.photoArtistService = photoArtistService;
    }

    //F1
    @GetMapping("/artist/{artistId}")
    public List<Photo> getPhotosByArtist(@PathVariable int artistId) {
        logger.info("Fetching photos for artist ID: {}", artistId);
        return photoArtistService.getPhotosByArtist(artistId);
    }

    //F2
    @GetMapping("/highrated")
    public List<Photo> getHighlyRatedPhotos() {
        logger.info("Fetching photos rated above 4 stars.");
        return highRatingService.getHighRatedPhotos();
    }

    //F3
    @GetMapping("/day")
    public DayOfWeek getMostCommonRatingDay() {
        logger.info("Fetching the most common rating day.");
        return ratedDayService.getMostRatedDay();
    }

    //F4
    @GetMapping("/average/{artistId}")
    public double getAverageRatingByArtist(@PathVariable int artistId) {
        logger.info("Fetching average rating for artist ID: {}", artistId);
        return ratingArtistService.getAverageRatingByArtist(artistId);
    }
}
