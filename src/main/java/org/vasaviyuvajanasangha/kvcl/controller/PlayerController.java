package org.vasaviyuvajanasangha.kvcl.controller;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.vasaviyuvajanasangha.kvcl.model.AppUser;
import org.vasaviyuvajanasangha.kvcl.model.Player;
import org.vasaviyuvajanasangha.kvcl.model.Team;
import org.vasaviyuvajanasangha.kvcl.pdf.DemoDocument;
import org.vasaviyuvajanasangha.kvcl.service.AppUserServiceImpl;
import org.vasaviyuvajanasangha.kvcl.service.PlayerServiceImpl;
import org.vasaviyuvajanasangha.kvcl.service.TeamServiceImpl;
import org.vasaviyuvajanasangha.kvcl.utils.ImageResizer;

import javax.imageio.ImageIO;

@Controller
@SessionAttributes({ "name", "username", "announcement" })
public class PlayerController {

	@Autowired
	private PlayerServiceImpl playerServiceImpl;

	@Autowired
	private TeamServiceImpl teamServiceImpl;

	@Autowired
	private AppUserServiceImpl appUserServiceImpl;

	@Autowired
	DemoDocument demoDocument;

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
		var present = playerServiceImpl.findPlayerByPhone(player.getPlayerPhone());
		if(!present.isPresent()){
			player.setTeam(team.get());
			AppUser user = appUserServiceImpl.getUserFromUserName(TeamController.getCurrentUser()).get();
			player.setPlayerPhone(user.getUsername());
			player.setPlayerEmail(user.getEmail());
			player.setTeamApproval(false);
			playerServiceImpl.savePlayer(player);
		}
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
		var present = playerServiceImpl.findPlayerByPhone(player.getPlayerPhone());
		if(!present.isPresent()) {
			AppUser user = appUserServiceImpl.getUserFromUserName(TeamController.getCurrentUser()).get();
			player.setTeamName(team.get().getName());
			player.setPlayerPhone(user.getUsername());
			player.setPlayerEmail(user.getEmail());
			player.setTeamApproval(true);
			playerServiceImpl.savePlayer(player);
		}
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
		return "editPlayer";
	}

	@PostMapping("/user/edit-player/{playerId}")
	public String updateUserInTeam(ModelMap model, @PathVariable Long playerId, Player player, BindingResult results) {
		var cur = TeamController.getCurrentUser();
		var team = teamServiceImpl.findTeamByName(player.getTeamName());
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
		playerServiceImpl.updatePlayer(dbPlayer);

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
		return "editCaptain";
	}

	@PostMapping("/user/edit-captain/{playerId}")
	public String updateCaptainInfo(ModelMap model, @PathVariable Long playerId, Player player, BindingResult results) {
		var team = teamServiceImpl.findTeamByName(player.getTeamName());
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
		playerServiceImpl.updatePlayer(dbPlayer);

		return "redirect:/user-home";
	}

	@GetMapping("/user/delete-player/{playerId}")
	public String deletePlayer(ModelMap model, @PathVariable Long playerId) {
		playerServiceImpl.deletePlayerById(playerId);

		return "redirect:/user-home";
	}

	@GetMapping("/user/resizeImages")
	@ResponseBody
	public String fixSizes(){
//		var players = playerServiceImpl.findPlayerById(254L);
		var players = playerServiceImpl.findAllPlayers();
		players.stream().forEach(playerServiceImpl::saveResizedImage);
//		playerServiceImpl.saveResizedImage(players);
		return "success";
	}

	@GetMapping("/user/download")
	public ResponseEntity downloadRegistrationForm(){
		String playerName = TeamController.getCurrentUser();
		var player = playerServiceImpl.findPlayerByPhone(playerName);
		Team team = teamServiceImpl.findTeamByName(player.get().getTeamName()).get();
		try(ByteArrayOutputStream pdfStream = demoDocument.generateDocument(team)){
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_PDF);
			headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=kvcl-2024-team-registration.pdf");
			headers.setContentLength(pdfStream.size());
			return new ResponseEntity<>(pdfStream.toByteArray(), headers, HttpStatus.OK);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@GetMapping("/user/form")
	public String downloadRegistrationForm(ModelMap model){
		String playerName = TeamController.getCurrentUser();
		var player = playerServiceImpl.findPlayerByPhone(playerName);
		Team team = teamServiceImpl.findTeamByName(player.get().getTeamName()).get();
		model.put("team",team);
		return "registrationPdf";
	}

}
