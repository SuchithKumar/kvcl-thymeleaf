package org.vasaviyuvajanasangha.kvcl.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.vasaviyuvajanasangha.kvcl.model.AppUser;
import org.vasaviyuvajanasangha.kvcl.model.Player;
import org.vasaviyuvajanasangha.kvcl.service.AppUserServiceImpl;
import org.vasaviyuvajanasangha.kvcl.service.PlayerServiceImpl;
import org.vasaviyuvajanasangha.kvcl.service.TeamServiceImpl;

@Controller
@SessionAttributes({ "name", "username", "team", "players", "announcement", "profile" })
public class PlayerController {

	@Autowired
	private PlayerServiceImpl playerServiceImpl;

	@Autowired
	private TeamServiceImpl teamServiceImpl;

	@Autowired
	private AppUserServiceImpl appUserServiceImpl;

	@GetMapping("/user/add-player")
	public String addUserToTeam(ModelMap model) {
		model.put("allteams", teamServiceImpl.findAllTeams().stream()
				.filter(a -> a.getPaymentInfoDB() != null && a.getVsDetails() != null).map(a -> a.getName()).toList());
		model.put("bloodGroups", List.of("O Positive", "O Negative", "A Positive", "A Negative", "B Positive",
				"B Negative", "AB Positive", "AB Negative"));
		model.put("battingStyles", List.of("None", "Right Hand Batsmen", "Left Hand Batsmen"));
		model.put("bowlingStyles",
				List.of("None", "Right Arm Med Fast", "Left Arm Med Fast", "Right Arm Spin", "Left Arm Spin"));

		model.put("player", new Player());
		return "addPlayer";

	}

	@PostMapping("/user/add-player")
	public String updateUserInTeam(ModelMap model, Player player, BindingResult results) {

		var team = teamServiceImpl.findTeamByName(player.getTeamName());
		player.setTeam(team.get());
		AppUser user = appUserServiceImpl.getUserFromUserName(TeamController.getCurrentUser()).get();
		player.setPlayerPhone(user.getUsername());
		player.setPlayerEmail(user.getEmail());
		player.setTeamApproval(false);
		playerServiceImpl.savePlayer(player);

		return "redirect:/user-home";
	}

	@GetMapping("/user/add-captain")
	public String addCaptainToTeam(ModelMap model) {
		var team = teamServiceImpl.findTeamByRegisterUser(TeamController.getCurrentUser());
		var player = new Player();
		player.setTeam(team.get());
		player.setTeamName(team.get().getName());
		model.put("player", player);
		model.put("bloodGroups", List.of("O Positive", "O Negative", "A Positive", "A Negative", "B Positive",
				"B Negative", "AB Positive", "AB Negative"));
		model.put("battingStyles", List.of("None", "Right Hand Batsmen", "Left Hand Batsmen"));
		model.put("bowlingStyles",
				List.of("None", "Right Arm Med Fast", "Left Arm Med Fast", "Right Arm Spin", "Left Arm Spin"));

		return "addCaptain";
	}

	@PostMapping("/user/add-captain")
	public String updateCaptainInTeam(ModelMap model, Player player, BindingResult results) {

		var team = teamServiceImpl.findTeamByRegisterUser(TeamController.getCurrentUser());
		player.setTeam(team.get());
		AppUser user = appUserServiceImpl.getUserFromUserName(TeamController.getCurrentUser()).get();
		player.setTeamName(team.get().getName());
		player.setPlayerPhone(user.getUsername());
		player.setPlayerEmail(user.getEmail());
		player.setTeamApproval(true);
		playerServiceImpl.savePlayer(player);

		return "redirect:/user-home";
	}

	@GetMapping("/user/approve-player/{playerId}")
	public String approvedPlayerToTeam(RedirectAttributes model, @PathVariable Long playerId) {
		var user = appUserServiceImpl.getUserFromUserName(TeamController.getCurrentUser()).get();
		var team = teamServiceImpl.findTeamByRegisterUser(user.getUsername()).get();
		var player = playerServiceImpl.findPlayerById(playerId);

		var approvedPlayers = team.getPlayers().stream()
				.filter(a -> a.getTeamApproval() != null && a.getTeamApproval().equals(true)).toList();
		int maxPlayers = 15;
		if (approvedPlayers.size() < maxPlayers) {
			if (user.getIsCaptain() && team.getName().equalsIgnoreCase(player.getTeamName())) {

				player.setTeamApproval(true);
				playerServiceImpl.savePlayerIgnoreImg(player);
			}
		}else {
			model.addFlashAttribute("approvalError", "Can't add more than "+ maxPlayers + " players into a Team");
		}

		return "redirect:/user-home";
	}

	@GetMapping("/user/reject-player/{playerId}")
	public String rejectPlayerToTeam(ModelMap model, @PathVariable Long playerId) {
		var user = appUserServiceImpl.getUserFromUserName(TeamController.getCurrentUser()).get();
		var team = teamServiceImpl.findTeamByRegisterUser(user.getUsername()).get();
		var player = playerServiceImpl.findPlayerById(playerId);

		if (user.getIsCaptain() && team.getName().equalsIgnoreCase(player.getTeamName())) {
			player.setTeamApproval(false);
			playerServiceImpl.savePlayerIgnoreImg(player);
		}

		return "redirect:/user-home";
	}

	@GetMapping("/user/edit-player/{playerId}")
	public String editPlayerInfo(ModelMap model, @PathVariable Long playerId) {
		model.put("player", playerServiceImpl.findPlayerById(playerId));
		model.put("allteams", teamServiceImpl.findAllTeams().stream().map(a -> a.getName()).toList());
		model.put("bloodGroups", List.of("O Positive", "O Negative", "A Positive", "A Negative", "B Positive",
				"B Negative", "AB Positive", "AB Negative"));
		model.put("battingStyles", List.of("None", "Right Hand Batsmen", "Left Hand Batsmen"));
		model.put("bowlingStyles",
				List.of("None", "Right Arm Med Fast", "Left Arm Med Fast", "Right Arm Spin", "Left Arm Spin"));
		return "addPlayer";
	}

	@PostMapping("/user/edit-player/{playerId}")
	public String updateUserInTeam(ModelMap model, @PathVariable Long playerId, Player player, BindingResult results) {
		var team = teamServiceImpl.findTeamByRegisterUser(TeamController.getCurrentUser());
		var dbPlayer = playerServiceImpl.findPlayerById(playerId);
		try {
			dbPlayer.setPlayerPhoto(player.getPhoto().getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		dbPlayer.setTeam(team.get());
		AppUser user = appUserServiceImpl.getUserFromUserName(TeamController.getCurrentUser()).get();
		dbPlayer.setPlayerPhone(user.getUsername());
		dbPlayer.setPlayerEmail(user.getEmail());
		dbPlayer.setTeamApproval(false);
		playerServiceImpl.savePlayer(dbPlayer);

		return "redirect:/user-home";
	}

	@GetMapping("/user/edit-captain/{playerId}")
	public String editCaptainInfo(ModelMap model, @PathVariable Long playerId) {
		model.put("player", playerServiceImpl.findPlayerById(playerId));
		model.put("bloodGroups", List.of("O Positive", "O Negative", "A Positive", "A Negative", "B Positive",
				"B Negative", "AB Positive", "AB Negative"));
		model.put("battingStyles", List.of("None", "Right Hand Batsmen", "Left Hand Batsmen"));
		model.put("bowlingStyles",
				List.of("None", "Right Arm Med Fast", "Left Arm Med Fast", "Right Arm Spin", "Left Arm Spin"));
		return "addCaptain";
	}

	@PostMapping("/user/edit-captain/{playerId}")
	public String updateCaptainInfo(ModelMap model, @PathVariable Long playerId, Player player, BindingResult results) {
		var team = teamServiceImpl.findTeamByRegisterUser(TeamController.getCurrentUser());
		var dbPlayer = playerServiceImpl.findPlayerById(playerId);
		try {
			dbPlayer.setPlayerPhoto(player.getPhoto().getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		dbPlayer.setTeam(team.get());
		AppUser user = appUserServiceImpl.getUserFromUserName(TeamController.getCurrentUser()).get();
		dbPlayer.setPlayerPhone(user.getUsername());
		dbPlayer.setPlayerEmail(user.getEmail());
		dbPlayer.setTeamApproval(true);
		playerServiceImpl.savePlayer(dbPlayer);

		return "redirect:/user-home";
	}

	@GetMapping("/user/delete-player/{playerId}")
	public String deletePlayer(ModelMap model, @PathVariable Long playerId) {
		playerServiceImpl.deletePlayerById(playerId);

		return "redirect:/user-home";
	}

}
