package org.vasaviyuvajanasangha.kvcl.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vasaviyuvajanasangha.kvcl.model.Announcement;

public interface AnnouncementsRepo extends JpaRepository<Announcement, Long>{
	
	Optional<Announcement> findTopByOrderByAnnouncedDateDesc();

}
