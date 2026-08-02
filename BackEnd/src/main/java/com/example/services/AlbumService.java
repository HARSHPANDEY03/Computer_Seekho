package com.example.services;

import java.util.List;

import com.example.dto.AlbumDTO;
import com.example.dto.AlbumDTO;

public interface AlbumService {

    List<AlbumDTO> getAllAlbums();

    AlbumDTO getAlbumById(int albumId);

    AlbumDTO saveAlbum(AlbumDTO albumRequest);

    AlbumDTO updateAlbum(int albumId, AlbumDTO albumRequest);

    void deleteAlbum(int albumId);
}