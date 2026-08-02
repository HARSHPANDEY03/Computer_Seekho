package com.example.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dto.ImageRequest;
import com.example.dto.ImageResponse;
import com.example.entities.Album;
import com.example.entities.Image;
import com.example.repositories.AlbumRepository;
import com.example.repositories.ImageRepository;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private AlbumRepository albumRepository;

    @Override
    public List<ImageResponse> getAllImages() {

        List<Image> images = imageRepository.findAll();
        List<ImageResponse> responses = new ArrayList<>();

        for (Image image : images) {
            responses.add(convertToResponse(image));
        }

        return responses;
    }

    @Override
    public ImageResponse getImageById(int imageId) {

        Image image = imageRepository.findById(imageId).orElse(null);

        if (image == null) {
            return null;
        }

        return convertToResponse(image);
    }

    @Override
    public ImageResponse saveImage(ImageRequest request) {

        Album album = albumRepository.findById(request.getAlbumId()).orElse(null);

        if (album == null) {
            return null;
        }

        Image image = new Image();
        image.setImagePath(request.getImagePath());
        image.setAlbum(album);
        image.setAlbumCover(request.isAlbumCover());
        image.setImageIsActive(request.isImageIsActive());

        Image savedImage = imageRepository.save(image);

        return convertToResponse(savedImage);
    }

    @Override
    public ImageResponse updateImage(int imageId, ImageRequest request) {

        Image image = imageRepository.findById(imageId).orElse(null);

        if (image == null) {
            return null;
        }

        Album album = albumRepository.findById(request.getAlbumId()).orElse(null);

        if (album == null) {
            return null;
        }

        image.setImagePath(request.getImagePath());
        image.setAlbum(album);
        image.setAlbumCover(request.isAlbumCover());
        image.setImageIsActive(request.isImageIsActive());

        Image updatedImage = imageRepository.save(image);

        return convertToResponse(updatedImage);
    }

    @Override
    public void deleteImage(int imageId) {
        imageRepository.deleteById(imageId);
    }

    private ImageResponse convertToResponse(Image image) {

        ImageResponse response = new ImageResponse();

        response.setImageId(image.getImageId());
        response.setImagePath(image.getImagePath());
        response.setAlbumCover(image.isAlbumCover());
        response.setImageIsActive(image.isImageIsActive());

        if (image.getAlbum() != null) {
            response.setAlbumId(image.getAlbum().getAlbumId());
            response.setAlbumName(image.getAlbum().getAlbumName());
        }

        return response;
    }
}