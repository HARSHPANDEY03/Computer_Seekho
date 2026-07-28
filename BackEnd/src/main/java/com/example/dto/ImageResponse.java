package com.example.dto;

public class ImageResponse {

    private Integer imageId;
    private String imagePath;
    private Integer albumId;
    private String albumName;
    private boolean isAlbumCover;
    private boolean imageIsActive;

    public ImageResponse() {
    }

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
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

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
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