package com.example.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.entities.Announcement;
import com.example.services.AnnouncementService;

@RestController
@RequestMapping("/announcements")
public class AnnouncementController {

    @Autowired
    private AnnouncementService announcementService;

    @GetMapping
    public List<Announcement> getAllAnnouncements() {
        return announcementService.getAllAnnouncements();
    }

    @GetMapping("/{id}")
    public Announcement getAnnouncementById(@PathVariable int id) {
        return announcementService.getAnnouncementById(id);
    }

    @PostMapping
    public Announcement saveAnnouncement(@RequestBody Announcement announcement) {
        return announcementService.saveAnnouncement(announcement);
    }

    @PutMapping("/{id}")
    public Announcement updateAnnouncement(@PathVariable int id,
                                           @RequestBody Announcement announcement) {
        return announcementService.updateAnnouncement(id, announcement);
    }

    @DeleteMapping("/{id}")
    public String deleteAnnouncement(@PathVariable int id) {
        announcementService.deleteAnnouncement(id);
        return "Announcement deleted successfully.";
    }
}