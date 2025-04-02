package com.photogallery.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class rating {
    private int id;
    private int memberID;
    private int photoID;
    private int ratingValue;
    private LocalDateTime ratingDate;

    public rating (@JsonProperty("id") int id,
                        @JsonProperty("memberID") int memberID,
                        @JsonProperty("photoID") int photoID,
                        @JsonProperty("ratingValue") int ratingValue,
                        @JsonProperty("ratingDate") String ratingDate) {
        this.id = id;
        this.memberID = memberID;
        this.photoID = photoID;
        this.ratingValue = ratingValue;
        this.ratingDate = LocalDateTime.parse(ratingDate.substring(0, 19));
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMemberID() {
        return memberID;
    }

    public void setMemberID(int memberID) {
        this.memberID = memberID;
    }

    public int getPhotoID() {
        return photoID;
    }

    public void setPhotoID(int photoID) {
        this.photoID = photoID;
    }

    public int getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(int ratingValue) {
        this.ratingValue = ratingValue;
    }

    public LocalDateTime getRatingDate() {
        return ratingDate;
    }

    public void setRatingDate(LocalDateTime ratingDate) {
        this.ratingDate = ratingDate;
    }
}
