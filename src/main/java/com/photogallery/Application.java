package com.photogallery;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class Application {
    public static void main(String[] args) {
        List<PhotoModel> photos = fetchHighlyRatedPhotos();  // Fetch highly rated photos
        SwingUtilities.invokeLater(() -> new Application(photos));
    }

    private JFrame frame;
    private JPanel photoPanel;
    private JLabel instructionLabel;

    public Application(List<PhotoModel> photos) {
        frame = new JFrame("Photo Gallery");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLayout(new BorderLayout());

        // Panel to hold the photos
        photoPanel = new JPanel(new GridLayout(0, 3, 10, 10)); // 3 columns
        JScrollPane scrollPane = new JScrollPane(photoPanel);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Instruction label at the top
        instructionLabel = new JLabel("Click a photo to view details", SwingConstants.CENTER);
        instructionLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        frame.add(instructionLabel, BorderLayout.NORTH);

        // Add photos to the panel
        if (photos != null && !photos.isEmpty()) {
            for (PhotoModel photo : photos) {
                photoPanel.add(createPhotoCard(photo)); // Add photo cards
            }
        } else {
            photoPanel.add(new JLabel("No photos available."));
        }

        frame.setVisible(true);
    }

    private JPanel createPhotoCard(PhotoModel photo) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setPreferredSize(new Dimension(200, 100));
        card.setMaximumSize(new Dimension(200, 100));
        card.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel titleLabel = new JLabel("<html><b>" + photo.getTitle() + "</b></html>", SwingConstants.CENTER);
        JLabel memberLabel = new JLabel("Member: " + photo.getMemberID(), SwingConstants.CENTER);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(memberLabel, BorderLayout.SOUTH);

        // Click event to show detailed photo view
        card.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                showPhotoDetails(photo);
            }
        });

        return card;
    }

    private void showPhotoDetails(PhotoModel photo) {
        String message = "<html><b>Photo ID:</b> " + photo.getId() + "<br>" +
                "<b>Title:</b> " + photo.getTitle() + "<br>" +
                "<b>Date Taken:</b> " + photo.getDateTaken() + "<br>" +
                "<b>Location:</b> " + photo.getLocation() + "<br>" +
                "<b>Description:</b> " + photo.getDescription() + "<br>" +
                "<b>File Name:</b> " + photo.getFileName() + "<br>" +
                "<b>Member ID:</b> " + photo.getMemberID() + "</html>";

        JOptionPane.showMessageDialog(null, message, "Photo Details", JOptionPane.INFORMATION_MESSAGE);
    }

    private static List<PhotoModel> fetchHighlyRatedPhotos() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/highrated"))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.readValue(response.body(), new TypeReference<List<PhotoModel>>() {});
            } else {
                System.out.println("Failed to fetch highly rated photos: " + response.statusCode() + " " + response.body());
            }
        } catch (Exception ex) {
            System.out.println("Error fetching photos: " + ex.getMessage());
        }
        return null;
    }

    public static class PhotoModel {
        private int id;
        private String title;
        private String dateTaken;
        private String location;
        private String description;
        private String fileName;
        private int memberID;

        // Getters for the model fields
        public int getId() { return id; }
        public String getTitle() { return title; }
        public String getDateTaken() { return dateTaken; }
        public String getLocation() { return location; }
        public String getDescription() { return description; }
        public String getFileName() { return fileName; }
        public int getMemberID() { return memberID; }
    }
}

