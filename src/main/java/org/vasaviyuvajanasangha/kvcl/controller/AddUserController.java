package org.vasaviyuvajanasangha.kvcl.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.vasaviyuvajanasangha.kvcl.model.AppUser;
import org.vasaviyuvajanasangha.kvcl.service.AnnouncementServiceImpl;
import org.vasaviyuvajanasangha.kvcl.service.AppUserServiceImpl;
import org.vasaviyuvajanasangha.kvcl.service.TeamServiceImpl;

import jakarta.validation.Valid;

@Controller
@SessionAttributes(names = {"name","username","announcement"})
public class AddUserController {

	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private AppUserServiceImpl appUserService;
	
	@Autowired
	private AnnouncementServiceImpl announcementServiceImpl;
	
	@GetMapping(path = "/register-user")
	public String registerUser(ModelMap model) {
		model.put("user", new AppUser());
		return "registerUser";
	}
	
	
	@PostMapping(path = "/register-user")
	public String registerUser(ModelMap model,@Valid @ModelAttribute("user") AppUser user,BindingResult result) {
		
		if(appUserService.getUserFromUserName(user.getUsername()).isPresent()) {
			model.put("user", user);
			model.put("errr", user.getUsername()+" is already registered!");
			model.put("contact", "Contact us for password reset!");
			return "registerUser";
		}
		
		if(result.hasErrors()) {
			logger.debug("validation errors - {}",result);
			model.put("user", user);
			return "registerUser";
		}
		
		if(!user.getPassword().equals(user.getReEnterPassword())) {
			model.put("err","passwords not matching");
			model.put("user", user);
			return "registerUser";
		}
		
		appUserService.saveAppUser(user);
		model.put("name", user.getUsername());
		model.put("username", user.getName());
		model.put("announcement", announcementServiceImpl.getLastAnnouncement());
		return "registerSuccess";
	}
	
}
