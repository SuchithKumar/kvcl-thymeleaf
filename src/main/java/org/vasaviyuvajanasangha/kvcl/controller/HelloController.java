package org.vasaviyuvajanasangha.kvcl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {
	
	
	@GetMapping(path = {"/login"})
	public String login() {
		return "login";
	}
	
	@GetMapping(path = {"/admin-home"})
	public String adminHome() {
		return "adminHome";
	}
	
	@GetMapping(path = {"/"})
	public String helloWelcome() {
		return "welcome";
	}
	
	@GetMapping(path = {"/welcome"})
	public String welcome() {
		return "welcome";
	}
	
	@GetMapping(path = {"/welcome-private"})
	public String welcomePrivate() {
		return "welcomePrivate";
	}
}
