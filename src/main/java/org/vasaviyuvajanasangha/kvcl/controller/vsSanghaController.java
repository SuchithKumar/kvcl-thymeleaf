package org.vasaviyuvajanasangha.kvcl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.vasaviyuvajanasangha.kvcl.model.Team;
import org.vasaviyuvajanasangha.kvcl.model.VasaviSanghaDetails;
import org.vasaviyuvajanasangha.kvcl.service.TeamServiceImpl;
import org.vasaviyuvajanasangha.kvcl.service.VasaviSanghaDetailsServicesImpl;
import org.vasaviyuvajanasangha.kvcl.utils.Helper;

@Controller
public class vsSanghaController {
	
	@Autowired
	private TeamServiceImpl teamServiceImpl;
	
	@Autowired
	private VasaviSanghaDetailsServicesImpl vasaviSanghaDetailsServiceImpl;
	
	
	@GetMapping("/user/add-sangha-details")
	public String getVasaviSanghaDetailsPage(ModelMap model) {
		model.put("vsDetails", new VasaviSanghaDetails());
		model.put("states",Helper.statesOfIndia());
		return "addVasaviSanghaDetails";
	}
	
	
	@PostMapping("/user/add-sangha-details")
	public String postVasaviSanghaDetailsPage(ModelMap model,VasaviSanghaDetails vsDetails,BindingResult result) {
		Team dbTeam = teamServiceImpl.findTeamByRegisterUser(TeamController.getCurrentUser()).get();
		vsDetails.setTeam(dbTeam);
		vasaviSanghaDetailsServiceImpl.saveVsDetails(vsDetails);		
		return "redirect:/user-home";
	}
	
	@GetMapping("/user/edit-vasavi-sangha-details")
	public String getEditVasaviSanghaDetailsPage(ModelMap model) {
		Team dbTeam = teamServiceImpl.findTeamByRegisterUser(TeamController.getCurrentUser()).get();
		model.put("vsDetails", dbTeam.getVsDetails());

		if(dbTeam.getVsDetails()==null)
			model.put("vsDetails", new VasaviSanghaDetails());

		model.put("states",Helper.statesOfIndia());
		return "addVasaviSanghaDetails";
	}
	
	@PostMapping("/user/edit-vasavi-sangha-details")
	public String editVasaviSanghaDetailsPage(ModelMap model,VasaviSanghaDetails vsDetails,BindingResult result) {
		Team dbTeam = teamServiceImpl.findTeamByRegisterUser(TeamController.getCurrentUser()).get();
		dbTeam.setVsDetails(vsDetails);
		vsDetails.setTeam(dbTeam);
		vasaviSanghaDetailsServiceImpl.saveVsDetails(vsDetails);		
		return "redirect:/user-home";
	}
	
	
}
