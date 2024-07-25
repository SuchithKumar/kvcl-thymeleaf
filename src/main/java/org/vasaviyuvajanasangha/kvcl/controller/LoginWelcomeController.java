package org.vasaviyuvajanasangha.kvcl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.vasaviyuvajanasangha.kvcl.service.AnnouncementServiceImpl;

@Controller
@SessionAttributes("announcement")
public class LoginWelcomeController {
	
	@Autowired
	private AnnouncementServiceImpl anServiceImpl;
	
	
	@GetMapping(path = {"/login"})
	public String login() {
		return "login";
	}
		
	@GetMapping(path = {"/"})
	public String helloWelcome(ModelMap map) {
		map.put("announcement", anServiceImpl.getLastAnnouncement());
		return "welcome";
	}
	
	@GetMapping(path = {"/welcome"})
	public String welcome(ModelMap map) {
		map.put("announcement", anServiceImpl.getLastAnnouncement());
		return "welcomeWithCards";
	}
	
	@GetMapping(path = {"/rules-and-reg"})
	public String rulesAndReg(ModelMap map) {
		return "rulesAndRegulations.html";
	}
	
	@GetMapping(path = {"/about-us"})
	public String aboutUs(ModelMap map) {
		return "aboutUs.html";
	}
	
	@GetMapping(path = {"/welcome-private"})
	public String welcomePrivate(ModelMap map) {
		map.put("announcement", anServiceImpl.getLastAnnouncement());
		return "welcomePrivate";
	}
}
