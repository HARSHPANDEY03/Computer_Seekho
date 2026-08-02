package com.example.dto;

public class ImageRequest {

    private String imagePath;
    private Integer albumId;
    private boolean isAlbumCover;
    private boolean imageIsActive;

    public ImageRequest() {
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Integer getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Integer albumId) {
        this.albumId = albumId;
    }

    public boolean isAlbumCover() {
        return isAlbumCover;
    }

    public void setAlbumCover(boolean albumCover) {
        isAlbumCover = albumCover;
    }

    public boolean isImageIsActive() {
        return imageIsActive;
    }

    public void setImageIsActive(boolean imageIsActive) {
        this.imageIsActive = imageIsActive;
    }
}