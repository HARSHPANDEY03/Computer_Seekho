package com.example.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entities.Album;

public interface AlbumRepository extends JpaRepository<Album, Integer> {

}