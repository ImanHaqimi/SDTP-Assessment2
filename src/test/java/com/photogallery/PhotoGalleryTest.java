package com.photogallery;

import com.photogallery.controller.PhotoGalleryController;
import com.photogallery.model.Photo;
import com.photogallery.service.HighRatingService;
import com.photogallery.service.PhotoArtistService;
import com.photogallery.service.RatedDayService;
import com.photogallery.service.RatingArtistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PhotoGalleryController.class)
class PhotoGalleryTest {

    private static final Logger logger = Logger.getLogger(PhotoGalleryTest.class.getName());

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HighRatingService highRatedPhotosService;

    @MockBean
    private RatingArtistService averageArtistRatingService;

    @MockBean
    private RatedDayService mostRatedDayService;

    @MockBean
    private PhotoArtistService photoByArtistService;

    @BeforeEach
    void setup() {
        logger.info("\n-------------------------------");
        logger.info("Starting Unit Tests...");
        logger.info("-------------------------------\n");
    }

    // F1: Get Photos by Artist
    @Test
    void getPhotosByArtist_ShouldReturnList() throws Exception {
        logger.info("Running test: PhotosByArtist");

        List<Photo> mockPhotos = Arrays.asList(
                new Photo(1, "Forest Trail", "2024-03-20T08:00:00", "Forest", "Morning walk in forest", "forest.jpg", 201),
                new Photo(2, "Desert Dunes", "2024-03-21T16:45:00", "Desert", "Golden dunes view", "desert.jpg", 201)
        );

        when(photoByArtistService.getPhotosByArtist(201)).thenReturn(mockPhotos);

        mockMvc.perform(get("/api/artist/201"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Forest Trail"))
                .andExpect(jsonPath("$[1].title").value("Desert Dunes"));

        logger.info("✅ F1 Test Successful\n");
    }

    // F2: Get Photos Rated Above 4 Stars
    @Test
    void getHighlyRatedPhotos_ShouldReturnList() throws Exception {
        logger.info("Running test: HighlyRatedPhotos");

        List<Photo> mockPhotos = Arrays.asList(
                new Photo(3, "Waterfall", "2024-03-22T14:00:00", "Jungle", "Hidden waterfall spot", "waterfall.jpg", 202),
                new Photo(4, "Aurora", "2024-03-23T22:30:00", "Iceland", "Northern lights magic", "aurora.jpg", 203)
        );

        when(highRatedPhotosService.getHighRatedPhotos()).thenReturn(mockPhotos);

        mockMvc.perform(get("/api/highrated"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Waterfall"))
                .andExpect(jsonPath("$[1].title").value("Aurora"));

        logger.info("✅ F2 Test Successful\n");
    }

    // F3: Get Most Common Rating Day
    @Test
    void getMostCommonRatingDay_ShouldReturnDayOfWeek() throws Exception {
        logger.info("Running test: MostCommonRatingDay");

        when(mostRatedDayService.getMostRatedDay()).thenReturn(DayOfWeek.SUNDAY);

        mockMvc.perform(get("/api/day"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("SUNDAY"));

        logger.info("✅ F3 Test Successful\n");
    }

    // F4: Get Average Rating by Artist
    @Test
    void getAverageRatingByArtist_ShouldReturnDouble() throws Exception {
        logger.info("Running test: AverageRatingByArtist");

        when(averageArtistRatingService.getAverageRatingByArtist(201)).thenReturn(4.9);

        mockMvc.perform(get("/api/average/201"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(4.9));

        logger.info("✅ F4 Test Successful\n");
    }
}