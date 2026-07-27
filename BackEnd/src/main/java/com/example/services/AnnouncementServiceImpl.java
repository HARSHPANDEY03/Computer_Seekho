package com.example.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dto.AnnouncementRequest;
import com.example.dto.AnnouncementResponse;
import com.example.entities.Announcement;
import com.example.repositories.AnnouncementRepository;

@Service
public class AnnouncementServiceImpl implements AnnouncementService {

    @Autowired
    private AnnouncementRepository announcementRepository;

    @Override
    public List<AnnouncementResponse> getAllAnnouncements() {

        List<Announcement> announcements = announcementRepository.findAll();
        List<AnnouncementResponse> responses = new ArrayList<>();

        for (Announcement announcement : announcements) {
            responses.add(convertToResponse(announcement));
        }

        return responses;
    }

    @Override
    public AnnouncementResponse getAnnouncementById(int announcementId) {

        Announcement announcement =
                announcementRepository.findById(announcementId).orElse(null);

        if (announcement == null) {
            return null;
        }

        return convertToResponse(announcement);
    }

    @Override
    public AnnouncementResponse saveAnnouncement(AnnouncementRequest request) {

        Announcement announcement = new Announcement();

        announcement.setTitle(request.getTitle());
        announcement.setDescription(request.getDescription());
        announcement.setPublishDate(request.getPublishDate());
        announcement.setExpiryDate(request.getExpiryDate());

        Announcement savedAnnouncement =
                announcementRepository.save(announcement);

        return convertToResponse(savedAnnouncement);
    }

    @Override
    public AnnouncementResponse updateAnnouncement(int announcementId,
                                                   AnnouncementRequest request) {

        Announcement announcement =
                announcementRepository.findById(announcementId).orElse(null);

        if (announcement == null) {
            return null;
        }

        announcement.setTitle(request.getTitle());
        announcement.setDescription(request.getDescription());
        announcement.setPublishDate(request.getPublishDate());
        announcement.setExpiryDate(request.getExpiryDate());

        Announcement updatedAnnouncement =
                announcementRepository.save(announcement);

        return convertToResponse(updatedAnnouncement);
    }

    @Override
    public void deleteAnnouncement(int announcementId) {
        announcementRepository.deleteById(announcementId);
    }

    private AnnouncementResponse convertToResponse(Announcement announcement) {

        AnnouncementResponse response = new AnnouncementResponse();

        response.setAnnouncementId(announcement.getAnnouncementId());
        response.setTitle(announcement.getTitle());
        response.setDescription(announcement.getDescription());
        response.setPublishDate(announcement.getPublishDate());
        response.setExpiryDate(announcement.getExpiryDate());

        return response;
    }
}