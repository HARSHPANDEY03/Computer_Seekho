package com.example.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entities.Announcement;
import com.example.repositories.AnnouncementRepository;

@Service
public class AnnouncementServiceImpl implements AnnouncementService {

    @Autowired
    private AnnouncementRepository announcementRepository;

    @Override
    public List<Announcement> getAllAnnouncements() {
        return announcementRepository.findAll();
    }

    @Override
    public Announcement getAnnouncementById(int announcementId) {
        return announcementRepository.findById(announcementId).orElse(null);
    }

    @Override
    public Announcement saveAnnouncement(Announcement announcement) {
        return announcementRepository.save(announcement);
    }

    @Override
    public Announcement updateAnnouncement(int announcementId, Announcement announcement) {

        Announcement existingAnnouncement =
                announcementRepository.findById(announcementId).orElse(null);

        if (existingAnnouncement != null) {
            existingAnnouncement.setTitle(announcement.getTitle());
            existingAnnouncement.setDescription(announcement.getDescription());
            existingAnnouncement.setPublishDate(announcement.getPublishDate());
            existingAnnouncement.setExpiryDate(announcement.getExpiryDate());

            return announcementRepository.save(existingAnnouncement);
        }

        return null;
    }

    @Override
    public void deleteAnnouncement(int announcementId) {
        announcementRepository.deleteById(announcementId);
    }
}