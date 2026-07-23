package com.example.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entities.Album;
import com.example.repositories.AlbumRepository;

@Service
public class AlbumServiceImpl implements AlbumService {

    @Autowired
    private AlbumRepository albumRepository;

    @Override
    public List<Album> getAllAlbums() {
        return albumRepository.findAll();
    }

    @Override
    public Album getAlbumById(int albumId) {
        return albumRepository.findById(albumId).orElse(null);
    }

    @Override
    public Album saveAlbum(Album album) {
        return albumRepository.save(album);
    }

    @Override
    public Album updateAlbum(int albumId, Album album) {

        Album existingAlbum = albumRepository.findById(albumId).orElse(null);

        if (existingAlbum != null) {
            existingAlbum.setAlbumName(album.getAlbumName());
            existingAlbum.setAlbumDescription(album.getAlbumDescription());
            existingAlbum.setStartDate(album.getStartDate());
            existingAlbum.setEndDate(album.getEndDate());
            existingAlbum.setAlbumIsActive(album.isAlbumIsActive());

            return albumRepository.save(existingAlbum);
        }

        return null;
    }

    @Override
    public void deleteAlbum(int albumId) {
        albumRepository.deleteById(albumId);
    }
}