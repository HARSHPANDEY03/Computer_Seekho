package com.example.dto;

import java.time.LocalDateTime;

public class AlbumRequest {

    private String albumName;
    private String albumDescription;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean albumIsActive;

    public AlbumRequest() {
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getAlbumDescription() {
        return albumDescription;
    }

    public void setAlbumDescription(String albumDescription) {
        this.albumDescription = albumDescription;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public boolean isAlbumIsActive() {
        return albumIsActive;
    }

    public void setAlbumIsActive(boolean albumIsActive) {
        this.albumIsActive = albumIsActive;
    }
}