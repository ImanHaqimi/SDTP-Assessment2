package com.photogallery;  // Ensure package follows Java naming conventions

import com.photogallery.model.Photo;  // Changed to lowercase as requested
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.time.DayOfWeek;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test") // Use a test profile if needed
public class integrationtest {  // Removed underscore (Java convention)

    private static final Logger logger = Logger.getLogger(integrationtest.class.getName());

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    void setup() {
        logger.info("\n-------------------------------");
        logger.info("Starting Integration Tests...");
        logger.info("-------------------------------\n");
    }

    // F1: Test retrieving photos by artist
    @Test
    void testGetPhotosByArtist() {
        logger.info("Running test: GetPhotosByArtist");

        int artistId = 1;  // Fixed incorrect integer formatting
        ResponseEntity<Photo[]> response = restTemplate.getForEntity("/api/artist/" + artistId, Photo[].class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().length).isGreaterThan(0);

        logger.info("✅ F1 Test Successful\n");
    }

    // F2: Test retrieving high-rated photos
    @Test
    void testGetHighlyRatedPhotos() {
        logger.info("Running test: GetHighlyRatedPhotos");

        ResponseEntity<Photo[]> response = restTemplate.getForEntity("/api/highrated", Photo[].class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().length).isGreaterThan(0);

        logger.info("✅ F2 Test Successful\n");
    }

    // F3: Test retrieving the most common rating day
    @Test
    void testGetMostCommonRatingDay() {
        logger.info("Running test: GetMostCommonRatingDay");

        ResponseEntity<DayOfWeek> response = restTemplate.getForEntity("/api/day", DayOfWeek.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();

        logger.info("✅ F3 Test Successful\n");
    }

    // F4: Test retrieving the average rating of an artist
    @Test
    void testGetAverageRatingByArtist() {
        logger.info("Running test: GetAverageRatingByArtist");

        int artistId = 101;
        ResponseEntity<Double> response = restTemplate.getForEntity("/api/average/" + artistId, Double.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();

        logger.info("✅ F4 Test Successful\n");
    }
}
