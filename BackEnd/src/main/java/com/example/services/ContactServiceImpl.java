package com.example.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entities.Contact;
import com.example.repositories.ContactRepository;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Override
    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
    }

    @Override
    public Contact getContactById(int contactId) {
        return contactRepository.findById(contactId).orElse(null);
    }

    @Override
    public Contact saveContact(Contact contact) {
        return contactRepository.save(contact);
    }

    @Override
    public Contact updateContact(int contactId, Contact contact) {

        Contact existingContact = contactRepository.findById(contactId).orElse(null);

        if (existingContact != null) {
            existingContact.setName(contact.getName());
            existingContact.setEmail(contact.getEmail());
            existingContact.setMessage(contact.getMessage());

            return contactRepository.save(existingContact);
        }

        return null;
    }

    @Override
    public void deleteContact(int contactId) {
        contactRepository.deleteById(contactId);
    }
}