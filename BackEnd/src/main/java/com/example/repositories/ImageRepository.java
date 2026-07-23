package com.example.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entities.Image;

public interface ImageRepository extends JpaRepository<Image, Integer> {

}