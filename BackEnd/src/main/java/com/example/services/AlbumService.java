package com.example.services;

import java.util.List;

import com.example.entities.Album;

public interface AlbumService {

    List<Album> getAllAlbums();

    Album getAlbumById(int albumId);

    Album saveAlbum(Album album);

    Album updateAlbum(int albumId, Album album);

    void deleteAlbum(int albumId);
}