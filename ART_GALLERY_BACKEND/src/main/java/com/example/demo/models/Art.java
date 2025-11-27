package com.example.demo.models;

import jakarta.persistence.*;

@Entity
public class Art {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String artName;
    private String artDescription;
    private String artistName;
    private Double artCost;

    @Lob
    private byte[] artPicture;

    // ✅ GETTERS AND SETTERS ADDED
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getArtName() {
        return artName;
    }

    public void setArtName(String artName) {
        this.artName = artName;
    }

    public String getArtDescription() {
        return artDescription;
    }

    public void setArtDescription(String artDescription) {
        this.artDescription = artDescription;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public Double getArtCost() {
        return artCost;
    }

    public void setArtCost(Double artCost) {
        this.artCost = artCost;
    }

    public byte[] getArtPicture() {
        return artPicture;
    }

    public void setArtPicture(byte[] artPicture) {
        this.artPicture = artPicture;
    }
}
