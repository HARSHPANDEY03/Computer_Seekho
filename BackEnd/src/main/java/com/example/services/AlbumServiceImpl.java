package com.example.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dto.AlbumDTO;
import com.example.dto.AlbumDTO;
import com.example.entities.Album;
import com.example.repositories.AlbumRepository;

@Service
public class AlbumServiceImpl implements AlbumService {

    @Autowired
    private AlbumRepository albumRepository;

    @Override
    public List<AlbumDTO> getAllAlbums() {

        List<Album> albums = albumRepository.findAll();
        List<AlbumDTO> responses = new ArrayList<>();

        for (Album album : albums) {
            responses.add(convertToResponse(album));
        }

        return responses;
    }

    @Override
    public AlbumDTO getAlbumById(int albumId) {

        Album album = albumRepository.findById(albumId).orElse(null);

        if (album == null) {
            return null;
        }

        return convertToResponse(album);
    }

    @Override
    public AlbumDTO saveAlbum(AlbumDTO request) {

        Album album = new Album();

        album.setAlbumName(request.getAlbumName());
        album.setAlbumDescription(request.getAlbumDescription());
        album.setStartDate(request.getStartDate());
        album.setEndDate(request.getEndDate());
        album.setAlbumIsActive(request.isAlbumIsActive());

        Album savedAlbum = albumRepository.save(album);

        return convertToResponse(savedAlbum);
    }

    @Override
    public AlbumDTO updateAlbum(int albumId, AlbumDTO request) {

        Album album = albumRepository.findById(albumId).orElse(null);

        if (album == null) {
            return null;
        }

        album.setAlbumName(request.getAlbumName());
        album.setAlbumDescription(request.getAlbumDescription());
        album.setStartDate(request.getStartDate());
        album.setEndDate(request.getEndDate());
        album.setAlbumIsActive(request.isAlbumIsActive());

        Album updatedAlbum = albumRepository.save(album);

        return convertToResponse(updatedAlbum);
    }

    @Override
    public void deleteAlbum(int albumId) {
        albumRepository.deleteById(albumId);
    }

    private AlbumDTO convertToResponse(Album album) {

        AlbumDTO response = new AlbumDTO();

        response.setAlbumId(album.getAlbumId());
        response.setAlbumName(album.getAlbumName());
        response.setAlbumDescription(album.getAlbumDescription());
        response.setStartDate(album.getStartDate());
        response.setEndDate(album.getEndDate());
        response.setAlbumIsActive(album.isAlbumIsActive());

        return response;
    }
}