package com.example.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.entities.Contact;
import com.example.services.ContactService;

@RestController
@RequestMapping("/contacts")
public class ContactController {

    @Autowired
    private ContactService contactService;

    // Get all contacts
    @GetMapping
    public List<Contact> getAllContacts() {
        return contactService.getAllContacts();
    }

    // Get contact by ID
    @GetMapping("/{id}")
    public Contact getContactById(@PathVariable int id) {
        return contactService.getContactById(id);
    }

    // Save a new contact
    @PostMapping
    public Contact saveContact(@RequestBody Contact contact) {
        return contactService.saveContact(contact);
    }

    // Update an existing contact
    @PutMapping("/{id}")
    public Contact updateContact(@PathVariable int id, @RequestBody Contact contact) {
        return contactService.updateContact(id, contact);
    }

    // Delete a contact
    @DeleteMapping("/{id}")
    public String deleteContact(@PathVariable int id) {
        contactService.deleteContact(id);
        return "Contact deleted successfully.";
    }
}