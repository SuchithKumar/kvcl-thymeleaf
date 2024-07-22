package org.vasaviyuvajanasangha.kvcl.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vasaviyuvajanasangha.kvcl.model.Announcement;
import org.vasaviyuvajanasangha.kvcl.repository.AnnouncementsRepo;

@Service
public class AnnouncementServiceImpl {

	@Autowired
	private AnnouncementsRepo repo;
	
	
	public Announcement getLastAnnouncement() {
		Optional<Announcement> announcement = repo.findTopByOrderByAnnouncedDateDesc();
		if(announcement.isEmpty()) {
			repo.save(new Announcement( "Welcome to KVCL 2024 Website!","Admin" , LocalDateTime.now()));
			return repo.findTopByOrderByAnnouncedDateDesc().get();
		}
					
		return repo.findTopByOrderByAnnouncedDateDesc().get();
	}
	
	public Announcement save(Announcement ann) {
		return repo.save(ann);
	}
	
}
