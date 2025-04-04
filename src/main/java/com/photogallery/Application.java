package com.photogallery;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.stream.Collectors;

public class Application {
    private JFrame frame;
    private JPanel topPanel;
    private JTextField searchField;
    private JTabbedPane tabbedPane;
    private JPanel membersTab;
    private JPanel photosTab;
    private List<MemberModel> allMembers;
    private List<MemberModel> filteredMembers;
    private JPanel photoGridPanel;
    private MemberModel selectedMember;  // Track the selected member

    public static void main(String[] args) {
        List<MemberModel> members = fetchAllMembers();
        SwingUtilities.invokeLater(() -> new Application(members));
    }

    public Application(List<MemberModel> members) {
        allMembers = members;
        filteredMembers = members;

        // Initialize main frame
        frame = new JFrame("Photo Gallery");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);
        frame.setLayout(new BorderLayout());

        // Create top panel with search field
        topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.setPreferredSize(new Dimension(frame.getWidth(), 50));
        topPanel.setBackground(new Color(60, 60, 60));

        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setForeground(Color.WHITE);
        searchLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        searchField = new JTextField();
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchField.setPreferredSize(new Dimension(250, 30));
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterResults(); // Trigger filtering on every key release
            }
        });

        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBackground(new Color(60, 60, 60));
        searchPanel.add(searchLabel, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);

        topPanel.add(searchPanel, BorderLayout.CENTER);

        // Create the tabbed pane
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 16));
        tabbedPane.setBackground(new Color(240, 240, 240));

        // Members Tab
        membersTab = new JPanel();
        membersTab.setLayout(new BoxLayout(membersTab, BoxLayout.Y_AXIS));
        membersTab.setBackground(Color.WHITE);
        updateMemberList(filteredMembers);
        JScrollPane memberScroll = new JScrollPane(membersTab);
        memberScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        tabbedPane.addTab("Members", memberScroll);

        // Photos Tab
        photosTab = new JPanel();
        photosTab.setLayout(new BorderLayout());
        photoGridPanel = new JPanel();
        photoGridPanel.setLayout(new GridLayout(0, 3, 15, 15));
        photosTab.add(photoGridPanel, BorderLayout.CENTER);
        tabbedPane.addTab("Photos", photosTab);

        // Add the components to the main frame
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(tabbedPane, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    // Method to filter members based on the search query
    private void filterResults() {
        String query = searchField.getText().toLowerCase();
        filteredMembers = allMembers.stream()
                .filter(member -> member.getForenames().toLowerCase().contains(query) ||
                        member.getSurname().toLowerCase().contains(query))
                .collect(Collectors.toList());

        // Update the member list with filtered results
        updateMemberList(filteredMembers);
    }

    // Update the member list with search results
    private void updateMemberList(List<MemberModel> members) {
        membersTab.removeAll();

        if (members != null && !members.isEmpty()) {
            for (MemberModel member : members) {
                JButton memberButton = new JButton(member.getForenames());
                memberButton.setFont(new Font("Arial", Font.PLAIN, 14));
                memberButton.setForeground(Color.WHITE);
                memberButton.setBackground(new Color(50, 50, 50));
                memberButton.setFocusPainted(false);
                memberButton.setMaximumSize(new Dimension(200, 40));
                memberButton.setAlignmentX(Component.CENTER_ALIGNMENT);

                // On member selection, set the selected member and load photos
                memberButton.addActionListener(e -> onMemberSelected(member));

                membersTab.add(memberButton);
                membersTab.add(Box.createVerticalStrut(10));
            }
        } else {
            JLabel noResultsLabel = new JLabel("No members found.");
            noResultsLabel.setFont(new Font("Arial", Font.ITALIC, 16));
            noResultsLabel.setForeground(Color.GRAY);
            noResultsLabel.setHorizontalAlignment(SwingConstants.CENTER);
            membersTab.add(noResultsLabel);
        }

        membersTab.revalidate();
        membersTab.repaint();
    }

    // Method to load photos for the selected member
    private void onMemberSelected(MemberModel member) {
        selectedMember = member; // Store the selected member
        updatePhotoList(fetchPhotosByMember(selectedMember.getId())); // Load their photos
    }

    // Update the photo list displayed in the "Photos" tab
    private void updatePhotoList(List<PhotoModel> photos) {
        photoGridPanel.removeAll();

        if (photos != null && !photos.isEmpty()) {
            for (PhotoModel photo : photos) {
                JPanel photoCard = createPhotoCard(photo);
                photoGridPanel.add(photoCard);
            }
        } else {
            JLabel noResultsLabel = new JLabel("No photos found.");
            noResultsLabel.setFont(new Font("Arial", Font.ITALIC, 16));
            noResultsLabel.setForeground(Color.GRAY);
            noResultsLabel.setHorizontalAlignment(SwingConstants.CENTER);
            photoGridPanel.add(noResultsLabel);
        }

        photoGridPanel.revalidate();
        photoGridPanel.repaint();
    }

    // Create a card for each photo to display in the grid
    private JPanel createPhotoCard(PhotoModel photo) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setPreferredSize(new Dimension(240, 160));
        card.setMaximumSize(new Dimension(240, 160));
        card.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        card.setBackground(new Color(245, 245, 245));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Image placeholder (replace with actual images if necessary)
        JLabel thumbnail = new JLabel();
        thumbnail.setIcon(new ImageIcon("path/to/thumbnail.jpg"));
        thumbnail.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel titleLabel = new JLabel("<html><b>" + photo.getTitle() + "</b></html>", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 12));
        titleLabel.setForeground(new Color(70, 70, 70));

        card.add(thumbnail, BorderLayout.CENTER);
        card.add(titleLabel, BorderLayout.SOUTH);

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showPhotoDetails(photo); // Show details of the selected photo
            }
        });

        return card;
    }

    // Show details for the selected photo
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

    // Fetch photos for the selected member
    private static List<PhotoModel> fetchPhotosByMember(int memberID) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/artist/" + memberID))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.readValue(response.body(), new TypeReference<>() {});
            } else {
                System.out.println("Failed to fetch photos: " + response.statusCode() + " " + response.body());
            }
        } catch (Exception ex) {
            System.out.println("Error fetching photos: " + ex.getMessage());
        }
        return List.of();
    }

    // Fetch all members (same as before)
    private static List<MemberModel> fetchAllMembers() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/members"))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.readValue(response.body(), new TypeReference<>() {});
            } else {
                System.out.println("Failed to fetch members: " + response.statusCode() + " " + response.body());
            }
        } catch (Exception ex) {
            System.out.println("Error fetching members: " + ex.getMessage());
        }
        return List.of();
    }

    // Member model (same as before)
    public static class MemberModel {
        private int id;
        private String surname;
        private String forenames;
        private String email;

        public int getId() { return id; }
        public String getSurname() { return surname; }
        public String getForenames() { return forenames; }
        public String getEmail() { return email; }
    }

    // Photo model (same as before)
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
