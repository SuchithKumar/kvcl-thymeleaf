package org.vasaviyuvajanasangha.kvcl.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.vasaviyuvajanasangha.kvcl.model.Player;
import org.vasaviyuvajanasangha.kvcl.service.PlayerServiceImpl;
import org.vasaviyuvajanasangha.kvcl.service.TeamServiceImpl;

@Controller
@SessionAttributes({"name","username","team","players","announcement"})
public class PlayerController {

	@Autowired
	private PlayerServiceImpl playerServiceImpl;
	
	@Autowired
	private TeamServiceImpl teamServiceImpl;
	
	@GetMapping("/user/add-player")
	public String addUserToTeam(ModelMap model) {
		model.put("player", new Player());
		return "addPlayer";
		
	}
	
	@PostMapping("/user/add-player")
	public String updateUserInTeam(ModelMap model,Player player,BindingResult results) {
		var team = teamServiceImpl.findTeamByRegisterUser(TeamController.getCurrentUser());
		player.setTeam(team.get());
		playerServiceImpl.savePlayer(player);
		
		return "redirect:/user-home";
	}
	

	@GetMapping("/user/edit-player/{playerId}")
	public String editPlayerInfo(ModelMap model,@PathVariable Long playerId) {
		model.put("player", playerServiceImpl.findPlayerById(playerId));
		return "addPlayer";
	}
	
	@PostMapping("/user/edit-player/{playerId}")
	public String updateUserInTeam(ModelMap model,@PathVariable Long playerId,Player player,BindingResult results) {
		var team = teamServiceImpl.findTeamByRegisterUser(TeamController.getCurrentUser());
		var dbPlayer = playerServiceImpl.findPlayerById(playerId);
		try {
			dbPlayer.setPlayerPhoto(player.getPhoto().getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		dbPlayer.setTeam(team.get());
		playerServiceImpl.savePlayer(dbPlayer);
		
		return "redirect:/user-home";
	}
	
	@GetMapping("/user/delete-player/{playerId}")
	public String deletePlayer(ModelMap model,@PathVariable Long playerId) {
		playerServiceImpl.deletePlayerById(playerId);
		
		return "redirect:/user-home";
	}
	
}
