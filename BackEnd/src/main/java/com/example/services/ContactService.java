package com.example.services;

import java.util.List;

import com.example.dto.ContactRequest;
import com.example.dto.ContactResponse;

public interface ContactService {

    List<ContactResponse> getAllContacts();

    ContactResponse getContactById(int contactId);

    ContactResponse saveContact(ContactRequest contactRequest);

    ContactResponse updateContact(int contactId, ContactRequest contactRequest);

    void deleteContact(int contactId);
}