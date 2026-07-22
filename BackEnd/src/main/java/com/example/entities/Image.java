package com.example.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "image")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private int imageId;

    @Column(name = "image_path")
    private String imagePath;

    @ManyToOne
    @JoinColumn(name = "album_id")
    private Album album;

    @Column(name = "is_album_cover")
    private boolean isAlbumCover;

    @Column(name = "image_is_active")
    private boolean imageIsActive;

    // Default Constructor
    public Image() {
    }

    // Getters and Setters

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public boolean isAlbumCover() {
        return isAlbumCover;
    }

    public void setAlbumCover(boolean isAlbumCover) {
        this.isAlbumCover = isAlbumCover;
    }

    public boolean isImageIsActive() {
        return imageIsActive;
    }

    public void setImageIsActive(boolean imageIsActive) {
        this.imageIsActive = imageIsActive;
    }
}