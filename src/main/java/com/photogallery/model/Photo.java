package com.photogallery.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class Photo {
    private int id;
    private String title;
    private LocalDateTime dateTaken;
    private String location;
    private String description;
    private String fileName;
    private int memberID;

    public Photo(@JsonProperty("id") int id,
                 @JsonProperty("title") String title,
                 @JsonProperty("dateTaken") String dateTaken,
                 @JsonProperty("location") String location,
                 @JsonProperty("description") String description,
                 @JsonProperty("fileName") String fileName,
                 @JsonProperty("memberID") int memberID) {
        this.id = id;
        this.title = title;
        this.dateTaken = LocalDateTime.parse(dateTaken.substring(0, 19));
        this.location = location;
        this.description = description;
        this.fileName = fileName;
        this.memberID = memberID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getDateTaken() {
        return dateTaken;
    }

    public void setDateTaken(LocalDateTime dateTaken) {
        this.dateTaken = dateTaken;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getMemberID() {
        return memberID;
    }

    public void setMemberID(int memberID) {
        this.memberID = memberID;
    }
}