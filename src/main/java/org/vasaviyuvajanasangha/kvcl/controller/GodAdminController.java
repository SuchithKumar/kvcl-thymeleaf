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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.vasaviyuvajanasangha.kvcl.model.Announcement;
import org.vasaviyuvajanasangha.kvcl.model.Editable;
import org.vasaviyuvajanasangha.kvcl.service.AdminServiceImpl;
import org.vasaviyuvajanasangha.kvcl.service.AnnouncementServiceImpl;
import org.vasaviyuvajanasangha.kvcl.service.AppUserServiceImpl;
import org.vasaviyuvajanasangha.kvcl.service.EditableServiceImpl;
import org.vasaviyuvajanasangha.kvcl.service.TeamServiceImpl;

@Controller
@RequestMapping("/godadmin")
@SessionAttributes("announcement")
public class GodAdminController {
	
	@Autowired
	private AppUserServiceImpl appUserServiceImpl;

	@Autowired
	private AdminServiceImpl adminService;
	
	@Autowired
	private TeamServiceImpl teamServiceImpl;
	
	@Autowired
	private AnnouncementServiceImpl announcementsService;
	
	@Autowired
	private EditableServiceImpl editableServiceImpl;
	
	@GetMapping("/god-admin-home")
	public String getAllUsers(ModelMap model) {
		model.put("announcement", announcementsService.getLastAnnouncement());
		model.put("registered_users",adminService.findAllRegisteredUsers().stream().filter(a-> a.getRoles().contains("USER")).toList());
		model.put("registered_admins",adminService.findAllRegisteredUsers().stream().filter(a-> a.getRoles().equals("ADMIN")).toList());
		model.put("super_admins",adminService.findAllRegisteredUsers().stream().filter(a-> a.getRoles().equals("GODADMIN")).toList());

		var teams = teamServiceImpl.findAllTeams();
		model.put("registered_teams",teamServiceImpl.findAllTeams());
		model.put("vasavi_sangha_details", teams.stream().filter(a->a.getVsDetails()!=null).toList());
		return "godAdminHome";
	}
		
	@GetMapping("/grant-admin-access/{id}")
	public String grantAdminAccess(ModelMap model, @PathVariable String id ) {
		adminService.grantAdminAccess(id);		
		return "redirect:/godadmin/god-admin-home";
	}
	
	@GetMapping("/revoke-admin-access/{id}")
	public String revokeAdminAccess(ModelMap model, @PathVariable String id ) {
		adminService.revokeAdminAccess(id);		
		return "redirect:/godadmin/god-admin-home";
	}
	
	@GetMapping("/delete-user/{id}")
	public String deleteUser(ModelMap model, @PathVariable String id ) {
		adminService.deleteUser(id);		
		return "redirect:/godadmin/god-admin-home";
	}
	
	@GetMapping("/delete-team/{id}")
	public String deleteTeam(ModelMap model, @PathVariable Long id ) {
		adminService.deleteTeam(id);		
		return "redirect:/godadmin/god-admin-home";
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
	
	@PostMapping("/reset-password")
	public String resetPassword(@RequestParam("number") String number, @RequestParam("password") String password){
		 var user = appUserServiceImpl.getUserFromUserName(number).get();
		 user.setPassword(password);
		 appUserServiceImpl.saveAppUser(user);
		 return "redirect:/godadmin/god-admin-home";
		
	}
	
	@PostMapping("/save-editable")
	public String saveEditable(@RequestParam("editTeamDetails") Boolean editTeamDetails,
								@RequestParam("editVasaviMathaDetails") Boolean editVasaviMathaDetails, 
								@RequestParam("editPaymentDetails") Boolean editPaymentDetails) {
		 
		 editableServiceImpl.save(new Editable(editTeamDetails, editVasaviMathaDetails, editPaymentDetails, true, TeamController.getCurrentUser()));
		 return "redirect:/godadmin/god-admin-home";
		
	}
}
