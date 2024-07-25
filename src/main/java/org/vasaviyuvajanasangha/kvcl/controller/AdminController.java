package org.vasaviyuvajanasangha.kvcl.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.vasaviyuvajanasangha.kvcl.model.Announcement;
import org.vasaviyuvajanasangha.kvcl.service.AdminServiceImpl;
import org.vasaviyuvajanasangha.kvcl.service.AnnouncementServiceImpl;
import org.vasaviyuvajanasangha.kvcl.service.TeamServiceImpl;

@Controller
@RequestMapping("/admin")
@SessionAttributes("announcement")
public class AdminController {
	
	@Autowired
	private AdminServiceImpl adminService;
	
	@Autowired
	private TeamServiceImpl teamServiceImpl;
	
	@Autowired
	private AnnouncementServiceImpl announcementsService;
	
	@GetMapping("/admin-home")
	public String getAllUsers(ModelMap model) {
		model.put("announcement", announcementsService.getLastAnnouncement());
		var teams = teamServiceImpl.findAllTeams();
		model.put("registered_teams",teamServiceImpl.findAllTeams());
		model.put("vasavi_sangha_details", teams.stream().filter(a->a.getVsDetails()!=null).toList());
		return "adminHome";
	}	
	
	@GetMapping("/new-announcement")
	public String getNewAnnouncement(ModelMap model){
		model.put("announcement",new Announcement());
		return "newAnnouncement";
	}
	
	@PostMapping("/new-announcement")
	public String newAnnouncement(ModelMap model, @ModelAttribute("announcement") Announcement announcement,BindingResult results){
		if(results.hasErrors()) {
			model.put("announcement",announcement);
			return "newAnnouncement";
		}
		announcement.setMadeBy(TeamController.getCurrentUser());
		announcement.setAnnouncedDate(LocalDateTime.now());
		announcementsService.save(announcement);
		model.put("announcement", announcement);	
		return getAllUsers(model);
	}
		
}
