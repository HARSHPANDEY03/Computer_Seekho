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

import com.example.dto.ContactRequest;
import com.example.dto.ContactResponse;
import com.example.services.ContactService;

@RestController
@RequestMapping("/contacts")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @GetMapping
    public List<ContactResponse> getAllContacts() {
        return contactService.getAllContacts();
    }

    @GetMapping("/{id}")
    public ContactResponse getContactById(@PathVariable int id) {
        return contactService.getContactById(id);
    }

    @PostMapping
    public ContactResponse saveContact(@RequestBody ContactRequest contactRequest) {
        return contactService.saveContact(contactRequest);
    }

    @PutMapping("/{id}")
    public ContactResponse updateContact(@PathVariable int id,
                                         @RequestBody ContactRequest contactRequest) {
        return contactService.updateContact(id, contactRequest);
    }

    @DeleteMapping("/{id}")
    public String deleteContact(@PathVariable int id) {
        contactService.deleteContact(id);
        return "Contact deleted successfully.";
    }
}