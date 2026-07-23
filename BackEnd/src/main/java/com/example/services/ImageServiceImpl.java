package com.example.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entities.Image;
import com.example.repositories.ImageRepository;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ImageRepository imageRepository;

    @Override
    public List<Image> getAllImages() {
        return imageRepository.findAll();
    }

    @Override
    public Image getImageById(int imageId) {
        return imageRepository.findById(imageId).orElse(null);
    }

    @Override
    public Image saveImage(Image image) {
        return imageRepository.save(image);
    }

    @Override
    public Image updateImage(int imageId, Image image) {

        Image existingImage = imageRepository.findById(imageId).orElse(null);

        if (existingImage != null) {
            existingImage.setImagePath(image.getImagePath());
            existingImage.setAlbum(image.getAlbum());
            existingImage.setAlbumCover(image.isAlbumCover());
            existingImage.setImageIsActive(image.isImageIsActive());

            return imageRepository.save(existingImage);
        }

        return null;
    }

    @Override
    public void deleteImage(int imageId) {
        imageRepository.deleteById(imageId);
    }
}