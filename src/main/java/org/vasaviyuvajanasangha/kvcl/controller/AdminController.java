package org.vasaviyuvajanasangha.kvcl.controller;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.vasaviyuvajanasangha.kvcl.model.Announcement;
import org.vasaviyuvajanasangha.kvcl.service.AnnouncementServiceImpl;
import org.vasaviyuvajanasangha.kvcl.service.PlayerServiceImpl;
import org.vasaviyuvajanasangha.kvcl.service.TeamServiceImpl;

@Controller
@RequestMapping("/admin")
@SessionAttributes("announcement")
public class AdminController {
	
	@Autowired
	private PlayerServiceImpl playerServiceImpl;
	
	@Autowired
	private TeamServiceImpl teamServiceImpl;
	
	@Autowired
	private AnnouncementServiceImpl announcementsService;
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@GetMapping("/admin-home")
	public String getAllUsers(ModelMap model) {
		var playersApr = playerServiceImpl.getTeamApprovalReport();
		model.put("approvalTable", playersApr);
		model.put("allteams", teamServiceImpl.findAllTeams().stream()
				.filter(a -> a.getPaymentInfoDB() != null && a.getVsDetails() != null).map(a -> a.getName()).toList());
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
	
	@PostMapping("/fetch-team-players")
	public String fetchTeamPlayers(RedirectAttributes redirectAttributes,@RequestParam("selectedTeamName") String selectedTeamName) {
		var players = teamServiceImpl.findTeamByName(selectedTeamName).get().getPlayers();
		redirectAttributes.addFlashAttribute("selectedTeamPlayers", players);
		return "redirect:/admin/admin-home";
	}
		
}
