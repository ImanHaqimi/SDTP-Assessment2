package com.photogallery;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Application {
    public static void main(String[] args) {
        List<PhotoModel> photos = fetchHighlyRatedPhotos();
        SwingUtilities.invokeLater(() -> new Application(photos));
    }

    private JFrame frame;
    private JPanel photoPanel;
    private JTextField searchField;
    private List<PhotoModel> allPhotos; // Stores original photos

    public Application(List<PhotoModel> photos) {
        this.allPhotos = (photos != null) ? photos : new ArrayList<>();

        frame = new JFrame("Photo Gallery");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(950, 600);
        frame.setLayout(new BorderLayout());

        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchField = new JTextField(30);
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchField.setToolTipText("Search by title...");
        searchPanel.add(new JLabel("🔍 Search: "));
        searchPanel.add(searchField);
        frame.add(searchPanel, BorderLayout.NORTH);

        // Photo Panel
        photoPanel = new JPanel(new GridLayout(0, 3, 15, 15));
        JScrollPane scrollPane = new JScrollPane(photoPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.add(scrollPane, BorderLayout.CENTER);

        // Populate photos initially
        updatePhotoDisplay(allPhotos);

        // Add search functionality
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filterPhotos(); }
            public void removeUpdate(DocumentEvent e) { filterPhotos(); }
            public void changedUpdate(DocumentEvent e) { filterPhotos(); }
        });

        frame.setVisible(true);
    }

    private void updatePhotoDisplay(List<PhotoModel> photos) {
        photoPanel.removeAll(); // Clear previous photos
        if (!photos.isEmpty()) {
            for (PhotoModel photo : photos) {
                photoPanel.add(createPhotoCard(photo));
            }
        } else {
            photoPanel.add(new JLabel("No matching photos found.", SwingConstants.CENTER));
        }
        photoPanel.revalidate();
        photoPanel.repaint();
    }

    private void filterPhotos() {
        String query = searchField.getText().trim().toLowerCase();
        List<PhotoModel> filteredPhotos = allPhotos.stream()
                .filter(photo -> photo.getTitle().toLowerCase().contains(query))
                .collect(Collectors.toList());

        updatePhotoDisplay(filteredPhotos);
    }

    private JPanel createPhotoCard(PhotoModel photo) {
        JPanel card = new JPanel(new BorderLayout());
        card.setPreferredSize(new Dimension(250, 120));
        card.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
        card.setBackground(new Color(230, 230, 230));

        JLabel titleLabel = new JLabel("<html><b>" + photo.getTitle() + "</b></html>", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel memberLabel = new JLabel("Member: " + photo.getMemberID(), SwingConstants.CENTER);
        memberLabel.setFont(new Font("Arial", Font.ITALIC, 12));

        card.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { card.setBackground(new Color(200, 200, 255)); }
            public void mouseExited(MouseEvent e) { card.setBackground(new Color(230, 230, 230)); }
            public void mouseClicked(MouseEvent e) { showPhotoDetails(photo); }
        });

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(memberLabel, BorderLayout.SOUTH);
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

        public int getId() { return id; }
        public String getTitle() { return title; }
        public String getDateTaken() { return dateTaken; }
        public String getLocation() { return location; }
        public String getDescription() { return description; }
        public String getFileName() { return fileName; }
        public int getMemberID() { return memberID; }
    }
}
