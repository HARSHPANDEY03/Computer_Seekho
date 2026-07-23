package com.example.services;

import java.util.List;

import com.example.entities.Announcement;

public interface AnnouncementService {

    List<Announcement> getAllAnnouncements();

    Announcement getAnnouncementById(int announcementId);

    Announcement saveAnnouncement(Announcement announcement);

    Announcement updateAnnouncement(int announcementId, Announcement announcement);

    void deleteAnnouncement(int announcementId);
}