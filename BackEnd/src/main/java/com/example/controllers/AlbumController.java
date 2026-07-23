package com.example.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.entities.Album;
import com.example.services.AlbumService;

@RestController
@RequestMapping("/albums")
public class AlbumController {

    @Autowired
    private AlbumService albumService;

    // Get all albums
    @GetMapping
    public List<Album> getAllAlbums() {
        return albumService.getAllAlbums();
    }

    // Get album by ID
    @GetMapping("/{id}")
    public Album getAlbumById(@PathVariable int id) {
        return albumService.getAlbumById(id);
    }

    // Save a new album
    @PostMapping
    public Album saveAlbum(@RequestBody Album album) {
        return albumService.saveAlbum(album);
    }

    // Update an existing album
    @PutMapping("/{id}")
    public Album updateAlbum(@PathVariable int id, @RequestBody Album album) {
        return albumService.updateAlbum(id, album);
    }

    // Delete an album
    @DeleteMapping("/{id}")
    public String deleteAlbum(@PathVariable int id) {
        albumService.deleteAlbum(id);
        return "Album deleted successfully.";
    }
}