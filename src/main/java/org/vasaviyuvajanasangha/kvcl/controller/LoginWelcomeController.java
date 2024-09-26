package org.vasaviyuvajanasangha.kvcl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.vasaviyuvajanasangha.kvcl.service.AnnouncementServiceImpl;
import org.vasaviyuvajanasangha.kvcl.service.EditableServiceImpl;
import org.vasaviyuvajanasangha.kvcl.service.PostService;

@Controller
@SessionAttributes("announcement")
public class LoginWelcomeController {

	@Autowired
	private EditableServiceImpl editableService;
	
	@Autowired
	private AnnouncementServiceImpl anServiceImpl;

	@Autowired
	private PostService postService;
	
	
	@GetMapping(path = {"/login"})
	public String login(ModelMap model) {
		model.put("announcement", anServiceImpl.getLastAnnouncement());
		model.put("editable", editableService.getLatestUpdate());

		return "login";
	}
		
	@GetMapping(path = {"/"})
	public String helloWelcome(ModelMap map) {
		map.put("announcement", anServiceImpl.getLastAnnouncement());
		map.put("editable", editableService.getLatestUpdate());
		return "welcomeWithCards";
	}
	
	@GetMapping(path = {"/welcome"})
	public String welcome(ModelMap map) {
		map.put("announcement", anServiceImpl.getLastAnnouncement());
		map.put("editable", editableService.getLatestUpdate());
		return "welcomeWithCards";
	}

	@GetMapping(path = {"/our-sponsors"})
	public String ourSponsors(ModelMap map) {
		map.put("announcement", anServiceImpl.getLastAnnouncement());
		map.put("editable", editableService.getLatestUpdate());
		return "ourSponsors";
	}

	@GetMapping(path = {"/our-sponsors-private"})
	public String ourSponsorsPrivate(ModelMap map) {
		map.put("announcement", anServiceImpl.getLastAnnouncement());
		map.put("editable", editableService.getLatestUpdate());
		return "ourSponsorsPrivate";
	}
	
	@GetMapping(path = {"/rules-and-reg"})
	public String rulesAndReg(ModelMap map) {
		return "rulesAndRegulations.html";
	}
	
	@GetMapping(path = {"/about-us"})
	public String aboutUs(ModelMap map) {
		return "aboutUs.html";
	}

	@GetMapping(path = {"/about-us-private"})
	public String aboutUsPrivate(ModelMap map) {
		return "aboutUsPrivate.html";
	}
	
	@GetMapping(path = {"/welcome-private"})
	public String welcomePrivate(ModelMap map) {
		map.put("announcement", anServiceImpl.getLastAnnouncement());
		map.put("editable", editableService.getLatestUpdate());
		return "welcomePrivate";
	}
	
	@GetMapping(path = {"/test"})
	public String testUs(ModelMap map) {
		return "test.html";
	}

	@GetMapping(path = {"/fixtures"})
	public String fixtures(ModelMap map) {
		map.put("announcement", anServiceImpl.getLastAnnouncement());
		map.put("editable", editableService.getLatestUpdate());
		return "fixtures";
	}

	@GetMapping(path = {"/updates"})
	public String updates(ModelMap map) {
		map.put("announcement", anServiceImpl.getLastAnnouncement());
		map.put("editable", editableService.getLatestUpdate());
		map.put("posts",postService.retrievePosts());
		return "updates";
	}

	@GetMapping(path = "/updates/delete/{id}")
	public String deleteUpdate(ModelMap map, @PathVariable Long id) {
		postService.removePost(postService.findPostByID(id));
		return "redirect:/updates";
	}
}
