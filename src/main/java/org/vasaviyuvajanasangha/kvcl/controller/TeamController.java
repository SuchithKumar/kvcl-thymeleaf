package org.vasaviyuvajanasangha.kvcl.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.vasaviyuvajanasangha.kvcl.model.FileEntity;
import org.vasaviyuvajanasangha.kvcl.model.Team;
import org.vasaviyuvajanasangha.kvcl.repository.AnnouncementsRepo;
import org.vasaviyuvajanasangha.kvcl.service.AnnouncementServiceImpl;
import org.vasaviyuvajanasangha.kvcl.service.TeamServiceImpl;

@Controller
@SessionAttributes({"name","username","team","players","announcement"})
public class TeamController {
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private TeamServiceImpl teamServiceImpl;
	
	@Autowired
	private AnnouncementServiceImpl anServiceImpl;
	
	@GetMapping(path = {"/user-home"})
	public String userHome(ModelMap model) {
		model.put("announcement", anServiceImpl.getLastAnnouncement());

		var teamPre = teamServiceImpl.findTeamByRegisterUser(getCurrentUser());
//		logger.info("update-request : {}",teamPre);
		if(teamPre.isEmpty()) {
			model.put("team", null);
			model.put("vsDetails", null);
			model.put("paymentDetails", null);
			return "userHome";
		}
		var team = teamPre.get();
		model.put("team", team);
		
		if(team.getVsDetails()!=null) {
			model.put("vsDetails", team.getVsDetails());
		}else {
			model.put("vsDetails", null);
		}
		
		if(team.getPaymentInfoDB()!=null) {
			model.put("paymentDetails", team.getPaymentInfoImg());
		}else {
			model.put("paymentDetails", null);
		}
		return "userHome";
	}
	

	@GetMapping("/user/register-team")
	public String viewTeamRegistrationPage(ModelMap model) {
		var teamPre = teamServiceImpl.findTeamByRegisterUser(getCurrentUser());
//		logger.debug("update-request : {}",teamPre);
		if(teamPre.isEmpty()) {
			model.put("team", new Team());
			return "addTeam";
		}
		var team = teamPre.get();
		model.put("team", team);
		
		return "addTeam";
	}
	
	@PostMapping("/user/register-team")
	public String saveTeamRegistration(ModelMap model,Team team,BindingResult results) {
		logger.debug("team object from form : {}",team);
		
		if(results.hasErrors()) {
			model.put("errors", results.getAllErrors());
		}
		
		team.setRegisteredUser(getCurrentUser());
		teamServiceImpl.saveTeamDetails(team);
		model.put("team",team);
		return "redirect:/user-home";
	}
	
	
	@GetMapping("/user/edit-team")
	public String editTeamInfo(ModelMap model) {
		model.put("team", teamServiceImpl.findTeamByRegisterUser(getCurrentUser()));
		return "addTeam";
	}
	
	@PostMapping("/user/edit-team")
	public String updateTeamDetails(ModelMap model,Team team,BindingResult results) {
		logger.info("team object from form : {}",team);
		
		if(results.hasErrors()) {
			model.put("errors", results.getAllErrors());
		}
		
		team.setRegisteredUser(getCurrentUser());
		teamServiceImpl.saveTeamDetails(team);
		model.put("team",team);
		
		return "redirect:/user-home";
	}
	
	@GetMapping("/user/add-payment-details")
	public String getPaymentDetailsPage(ModelMap model) {
		var teamPre = teamServiceImpl.findTeamByRegisterUser(getCurrentUser());
		if(!teamPre.isEmpty()) {
			model.put("paymentInfoFile", new FileEntity());
			return "addPaymentDetails";
		}
		return "redirect:/user-home";
	}
	
	@PostMapping("/user/add-payment-details")
	public String postPaymentDetailsPage(ModelMap model,FileEntity fileEntity,BindingResult results) {
		if(results.hasErrors()) {
			model.put("paymentInfoFile", fileEntity);
			return "addPaymentDetails";
		}
	
		Team dbTeam = teamServiceImpl.findTeamByRegisterUser(getCurrentUser()).get();
		if(dbTeam!=null) {
			try {
				dbTeam.setPaymentInfoDB(fileEntity.getFile().getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
			teamServiceImpl.saveTeamDetails(dbTeam);
			return "redirect:/user-home";
			
		}
		return "redirect:/user-home";
						
	}
	
	public static String getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();
		return currentPrincipalName;
	}
	
}
