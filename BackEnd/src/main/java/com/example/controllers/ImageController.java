package com.example.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.entities.Image;
import com.example.services.ImageService;

@RestController
@RequestMapping("/images")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @GetMapping
    public List<Image> getAllImages() {
        return imageService.getAllImages();
    }

    @GetMapping("/{id}")
    public Image getImageById(@PathVariable int id) {
        return imageService.getImageById(id);
    }

    @PostMapping
    public Image saveImage(@RequestBody Image image) {
        return imageService.saveImage(image);
    }

    @PutMapping("/{id}")
    public Image updateImage(@PathVariable int id, @RequestBody Image image) {
        return imageService.updateImage(id, image);
    }

    @DeleteMapping("/{id}")
    public String deleteImage(@PathVariable int id) {
        imageService.deleteImage(id);
        return "Image deleted successfully.";
    }
}