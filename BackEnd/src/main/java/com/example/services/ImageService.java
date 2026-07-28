package com.example.services;

import java.util.List;

import com.example.dto.ImageRequest;
import com.example.dto.ImageResponse;

public interface ImageService {

    List<ImageResponse> getAllImages();

    ImageResponse getImageById(int imageId);

    ImageResponse saveImage(ImageRequest imageRequest);

    ImageResponse updateImage(int imageId, ImageRequest imageRequest);

    void deleteImage(int imageId);
}