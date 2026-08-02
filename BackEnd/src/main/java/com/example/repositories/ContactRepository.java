package com.example.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.entities.Contact;

public interface ContactRepository extends JpaRepository<Contact, Integer> {

}