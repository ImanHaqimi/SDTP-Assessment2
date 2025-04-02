package com.photogallery.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class member {
    int id;
    String surname;
    String forenames;
    String email;

    public member(
            @JsonProperty("id") int id,
            @JsonProperty("surname") String surname,
            @JsonProperty("forenames") String forenames,
            @JsonProperty("email") String email) {
        this.id = id;
        this.surname = surname;
        this.forenames = forenames;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getForenames() {
        return forenames;
    }

    public void setForenames(String forenames) {
        this.forenames = forenames;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
