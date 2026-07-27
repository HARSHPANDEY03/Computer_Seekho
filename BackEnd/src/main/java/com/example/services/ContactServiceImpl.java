package com.example.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dto.ContactRequest;
import com.example.dto.ContactResponse;
import com.example.entities.Contact;
import com.example.repositories.ContactRepository;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Override
    public List<ContactResponse> getAllContacts() {

        List<Contact> contacts = contactRepository.findAll();
        List<ContactResponse> responses = new ArrayList<>();

        for (Contact contact : contacts) {
            responses.add(convertToResponse(contact));
        }

        return responses;
    }

    @Override
    public ContactResponse getContactById(int contactId) {

        Contact contact = contactRepository.findById(contactId).orElse(null);

        if (contact == null) {
            return null;
        }

        return convertToResponse(contact);
    }

    @Override
    public ContactResponse saveContact(ContactRequest request) {

        Contact contact = new Contact();
        contact.setName(request.getName());
        contact.setEmail(request.getEmail());
        contact.setMessage(request.getMessage());

        Contact savedContact = contactRepository.save(contact);

        return convertToResponse(savedContact);
    }

    @Override
    public ContactResponse updateContact(int contactId, ContactRequest request) {

        Contact contact = contactRepository.findById(contactId).orElse(null);

        if (contact == null) {
            return null;
        }

        contact.setName(request.getName());
        contact.setEmail(request.getEmail());
        contact.setMessage(request.getMessage());

        Contact updatedContact = contactRepository.save(contact);

        return convertToResponse(updatedContact);
    }

    @Override
    public void deleteContact(int contactId) {
        contactRepository.deleteById(contactId);
    }

    private ContactResponse convertToResponse(Contact contact) {

        ContactResponse response = new ContactResponse();

        response.setContactId(contact.getContactId());
        response.setName(contact.getName());
        response.setEmail(contact.getEmail());
        response.setMessage(contact.getMessage());

        return response;
    }
}