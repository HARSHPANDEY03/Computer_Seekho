package com.example.services;

import java.util.List;

import com.example.dto.AlbumRequest;
import com.example.dto.AlbumResponse;

public interface AlbumService {

    List<AlbumResponse> getAllAlbums();

    AlbumResponse getAlbumById(int albumId);

    AlbumResponse saveAlbum(AlbumRequest albumRequest);

    AlbumResponse updateAlbum(int albumId, AlbumRequest albumRequest);

    void deleteAlbum(int albumId);
}