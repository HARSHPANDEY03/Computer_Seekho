package com.example.services;

import java.util.List;

import com.example.entities.Image;

public interface ImageService {

    List<Image> getAllImages();

    Image getImageById(int imageId);

    Image saveImage(Image image);

    Image updateImage(int imageId, Image image);

    void deleteImage(int imageId);
}