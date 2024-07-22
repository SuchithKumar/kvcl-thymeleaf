package org.vasaviyuvajanasangha.kvcl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.vasaviyuvajanasangha.kvcl.service.AdminServiceImpl;
import org.vasaviyuvajanasangha.kvcl.service.TeamServiceImpl;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	private AdminServiceImpl adminService;
	
	@Autowired
	private TeamServiceImpl teamServiceImpl;
	
	@GetMapping("/admin-home")
	public String getAllUsers(ModelMap model) {
		model.put("registered_users",adminService.findAllRegisteredUsers());
		var teams = teamServiceImpl.findAllTeams();
		model.put("registered_teams",teamServiceImpl.findAllTeams());
		model.put("vasavi_sangha_details", teams.stream().filter(a->a.getVsDetails()!=null).toList());
		return "adminHome";
	}
		
	@GetMapping("/grant-admin-access/{id}")
	public String grantAdminAccess(ModelMap model, @PathVariable String id ) {
		adminService.grantAdminAccess(id);		
		return "redirect:/admin/admin-home";
	}
	
	@GetMapping("/revoke-admin-access/{id}")
	public String revokeAdminAccess(ModelMap model, @PathVariable String id ) {
		adminService.revokeAdminAccess(id);		
		return "redirect:/admin/admin-home";
	}
	
	@GetMapping("/delete-user/{id}")
	public String deleteUser(ModelMap model, @PathVariable String id ) {
		adminService.deleteUser(id);		
		return "redirect:/admin/admin-home";
	}
	
	
}
