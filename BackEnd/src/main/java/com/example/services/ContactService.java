package com.example.services;

import java.util.List;

import com.example.entities.Contact;

public interface ContactService {

    List<Contact> getAllContacts();

    Contact getContactById(int contactId);

    Contact saveContact(Contact contact);

    Contact updateContact(int contactId, Contact contact);

    void deleteContact(int contactId);
}