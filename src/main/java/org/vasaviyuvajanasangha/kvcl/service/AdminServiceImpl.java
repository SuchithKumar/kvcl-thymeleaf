package org.vasaviyuvajanasangha.kvcl.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vasaviyuvajanasangha.kvcl.model.Announcement;
import org.vasaviyuvajanasangha.kvcl.model.AppUser;
import org.vasaviyuvajanasangha.kvcl.model.Team;
import org.vasaviyuvajanasangha.kvcl.repository.AnnouncementsRepo;
import org.vasaviyuvajanasangha.kvcl.repository.IAppUserRepository;
import org.vasaviyuvajanasangha.kvcl.repository.TeamRepository;

import jakarta.transaction.Transactional;

@Service
public class AdminServiceImpl {
	
	@Autowired
	private IAppUserRepository usersRepo;
	
	@Autowired
	private TeamServiceImpl teamServiceImpl;
	
	@Autowired
	private TeamRepository teamRepo;
	
	@Autowired
	private AnnouncementsRepo announcementsRepo;
	
	
	public List<AppUser> findAllRegisteredUsers(){
		return usersRepo.findAll();
	}


	public void grantAdminAccess(String id) {
		AppUser user = usersRepo.findById(Long.valueOf(id)).get();
		user.setRoles("ADMIN");
		usersRepo.save(user);
		
	}

	public void revokeAdminAccess(String id) {
		AppUser user = usersRepo.findById(Long.valueOf(id)).get();
		user.setRoles("USER");
		usersRepo.save(user);
	}


	@Transactional
	public void deleteUser(String id) {
		AppUser user = usersRepo.findById(Long.valueOf(id)).get();
		Optional<Team> team = teamServiceImpl.findTeamByRegisterUser(user.getUsername());
		if(team.isPresent())
			teamRepo.delete(team.get());
		usersRepo.delete(user);
	}


	public void deleteTeam(Long id) {
		Team team = teamRepo.findById(id).get();
		teamRepo.delete(team);
	}

	
	public Optional<Announcement> lastAnnouncement() {
		return announcementsRepo.findTopByOrderByAnnouncedDateDesc();
	}
}
