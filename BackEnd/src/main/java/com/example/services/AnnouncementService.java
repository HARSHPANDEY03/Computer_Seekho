package com.example.services;

import java.util.List;

import com.example.dto.AnnouncementRequest;
import com.example.dto.AnnouncementResponse;

public interface AnnouncementService {

    List<AnnouncementResponse> getAllAnnouncements();

    AnnouncementResponse getAnnouncementById(int announcementId);

    AnnouncementResponse saveAnnouncement(AnnouncementRequest announcementRequest);

    AnnouncementResponse updateAnnouncement(int announcementId,
                                            AnnouncementRequest announcementRequest);

    void deleteAnnouncement(int announcementId);
}