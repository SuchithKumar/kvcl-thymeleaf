package org.vasaviyuvajanasangha.kvcl.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.vasaviyuvajanasangha.kvcl.model.*;
import org.vasaviyuvajanasangha.kvcl.pdf.DemoDocument;
import org.vasaviyuvajanasangha.kvcl.pdf.upload.service.FilesStorageService;
import org.vasaviyuvajanasangha.kvcl.repository.LikesRepo;
import org.vasaviyuvajanasangha.kvcl.service.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@SessionAttributes({ "name", "username", "announcement" })
public class PlayerController {

	Logger logger = LoggerFactory.getLogger(this.getClass().getName());
	@Autowired
	private PlayerServiceImpl playerServiceImpl;

	@Autowired
	private TeamServiceImpl teamServiceImpl;

	@Autowired
	private AppUserServiceImpl appUserServiceImpl;

	@Autowired
	private LikesRepo likesRepo;

	@Autowired
	private ImageService imageService;

	@Autowired
	DemoDocument demoDocument;

	@Autowired
	private FilesStorageService storageService;

	@Autowired
	private YoutubeService youtubeService;

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
			player.setLikes(0);
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
			player.setLikes(0);
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
		var team = teamServiceImpl.findTeamByName(player.getTeamName()).get();
		try {
			player.setPlayerPhoto(player.getPhoto().getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}

		AppUser user = appUserServiceImpl.getUserFromUserName(TeamController.getCurrentUser()).get();
		player.setPlayerPhone(user.getUsername());
		player.setPlayerEmail(user.getEmail());
		player.setTeamApproval(false);
		player.setTeam(team);
		playerServiceImpl.updatePlayer(player);

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
		var team = teamServiceImpl.findTeamByName(player.getTeamName()).get();
		var dbPlayer = playerServiceImpl.findPlayerById(playerId);
		try {
			player.setPlayerPhoto(player.getPhoto().getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}

		AppUser user = appUserServiceImpl.getUserFromUserName(TeamController.getCurrentUser()).get();
		player.setPlayerPhone(user.getUsername());
		player.setPlayerEmail(user.getEmail());
		player.setTeamApproval(true);
		player.setTeam(team);
		playerServiceImpl.updatePlayer(player);

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
	public ResponseEntity downloadRegistrationForm(@RequestParam(required = false) Optional<String> lang){
		String playerName = TeamController.getCurrentUser();
		var player = playerServiceImpl.findPlayerByPhone(playerName);
//		Team team = teamServiceImpl.findTeamByName(player.get().getTeamName()).get();
		Team team = teamServiceImpl.findTeamByName("Aviyuktas").get();
		String tournament_announcement =  imageService.getImg("tournament-announcement.png");
		String kannadaThanks = imageService.getImg("kvcl-kannada-thanks.png");
		String kannadaAnnouncement = imageService.getImg("kvcl-kannada-announcement2.png");
		String emptyCheckBox = imageService.getImg("square.png");
		String kvclPdfSponsors = imageService.getImg("kvcl-pdf-sponsors.png");
		List<String> images = List.of(tournament_announcement,kannadaThanks,kannadaAnnouncement,emptyCheckBox,kvclPdfSponsors);

		String template = "registrationPdf";
		String language = "english";
		if(lang.isPresent() && lang.get().equalsIgnoreCase("ka")){
			language = "kannada";
			template = "registrationPdfKannada";
		}

		team.setPlayers(team.getPlayers().stream().filter(a->a.getTeamApproval()==true).collect(Collectors.toList()));
		try(ByteArrayOutputStream pdfStream = demoDocument.generateDocument(template,team,images)){
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_PDF);
			headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=kvcl2024-declaration-form-"+language+".pdf");
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

		String tournament_announcement =  imageService.getImg("tournament-announcement.png");
		String kannadaThanks = imageService.getImg("kvcl-kannada-thanks.png");
		String kannadaAnnouncement = imageService.getImg("kvcl-kannada-announcement2.png");
		String emptyCheckBox = imageService.getImg("square.png");
		String kvclPdfSponsors = imageService.getImg("kvcl-pdf-sponsors.png");
		List<String> images = List.of(tournament_announcement,kannadaThanks,kannadaAnnouncement,emptyCheckBox,kvclPdfSponsors);
		model.put("images",images);

		return "registrationPdf";
	}

	@GetMapping("/user/formk")
	public String downloadRegistrationFormKannada(ModelMap model){
		String playerName = TeamController.getCurrentUser();
		var player = playerServiceImpl.findPlayerByPhone(playerName);
		Team team = teamServiceImpl.findTeamByName(player.get().getTeamName()).get();
		model.put("team",team);

		String tournament_announcement =  imageService.getImg("tournament-announcement.png");
		String kannadaThanks = imageService.getImg("kvcl-kannada-thanks.png");
		String kannadaAnnouncement = imageService.getImg("kvcl-kannada-announcement2.png");
		String emptyCheckBox = imageService.getImg("square.png");
		String kvclPdfSponsors = imageService.getImg("kvcl-pdf-sponsors.png");
		List<String> images = List.of(tournament_announcement,kannadaThanks,kannadaAnnouncement,emptyCheckBox,kvclPdfSponsors);

		model.put("images",images);

		return "registrationPdfKannada";
	}

//	working prototype of all squads
//	@GetMapping("/squads")
//	public String squads(ModelMap model){
//		List<Team> allTeams =  teamServiceImpl.findAllTeams();
//		List<Squad> squads = new ArrayList<>();
//		for(Team team : allTeams){
////			logger.info("setting team : {}",team.getName());
//
//			var captain = playerServiceImpl.findPlayerByPhone(team.getRegisteredUser());
//			if(captain.isPresent()) {
//				var approvedPlayers = team.getPlayers().stream().filter(a -> a != captain.get() && a.getTeamApproval() == true).collect(Collectors.toList());
//				squads.add(new Squad(team, captain.get(), approvedPlayers));
//			}
//		}
//		model.put("squads",squads);
//		return "privateSquads";
//	}

	@GetMapping("/squads/show-squad")
	public String showSquad(@RequestParam String selectedTeamName,@RequestParam(required = false) String msg, RedirectAttributes redirectAttributes, Model model){
		List<Squad> squads = new ArrayList<>();
		AppUser user = appUserServiceImpl.getUserFromUserName(TeamController.getCurrentUser()).get();
		List<Team> allTeams =  List.of(teamServiceImpl.findTeamByName(selectedTeamName).get());
		long count = 0;
		for(Team team : allTeams){
			count = team.getPlayers().stream().map(a->a.getLikes()).reduce(0,(a,b)->a+b);
//			logger.info("setting team : {}",team.getName());
			var captain = playerServiceImpl.findPlayerByPhone(team.getRegisteredUser());
			if(captain.isPresent()) {
				var approvedPlayers = team.getPlayers().stream().filter(a -> a != captain.get() && a.getTeamApproval() == true).collect(Collectors.toList());
				squads.add(new Squad(team, captain.get(), approvedPlayers));
			}
		}
		if(msg!=null && !msg.isEmpty())
			redirectAttributes.addFlashAttribute("msg",msg);
		redirectAttributes.addFlashAttribute("squads",squads);
		redirectAttributes.addFlashAttribute("count",count);
		redirectAttributes.addFlashAttribute("user",user);
		redirectAttributes.addFlashAttribute("sponsor",imageService.getImgSponsor(squads.get(0).getTeam().getSponsor()));
		return "redirect:/squads";
	}

	@GetMapping("/squads")
	public String squads(ModelMap model){
		List<String> teamNames = teamServiceImpl.findAllTeamNames();
		model.put("teamNames",teamNames);
		return "squads";
	}

	@GetMapping("/user/collab")
	public String collab(@RequestParam Long playerId,@RequestParam String selectedTeamName,RedirectAttributes model){
		var currentPlayer = playerServiceImpl.findPlayerByPhone(TeamController.getCurrentUser()).get();
		var currentPlayerTeam = teamServiceImpl.findTeamByName(currentPlayer.getTeamName());
		var alreadyCollabed = likesRepo.findByLikedPlayerIdAndPlayer(playerId,currentPlayer);

		var likedPlayer = playerServiceImpl.findPlayerById(playerId);
		var likedPlayerTeam = teamServiceImpl.findTeamByName(likedPlayer.getTeamName());
		var likedPlayerAlreadyCollabed = likesRepo.findByLikedPlayerIdAndPlayer(currentPlayer.getPlayerId(),likedPlayer);

		if(!currentPlayer.getTeamApproval()){
			return "redirect:/squads/show-squad?selectedTeamName="+selectedTeamName+"&msg=warning";
		}

		if(playerId==currentPlayer.getPlayerId()){
			model.addFlashAttribute("msg","error");
			return "redirect:/squads";
		}

		//same player or same team player collab check
		if(currentPlayerTeam.equals(likedPlayerTeam)){
			model.addFlashAttribute("msg","error");
			return "redirect:/squads";
		}


		if(alreadyCollabed.isPresent() && likedPlayerAlreadyCollabed.isPresent()){
			if(alreadyCollabed.get().isAccepted()&&likedPlayerAlreadyCollabed.get().isAccepted()==true){
				model.addFlashAttribute("msg","warning");
				return "redirect:/squads/show-squad?selectedTeamName="+selectedTeamName+"&msg=warning";
			}
		}


		if(!likedPlayerAlreadyCollabed.isPresent()){
			if(!alreadyCollabed.isPresent()){
				incrementLike(playerId);
				likesRepo.save(new Likes(playerId,LocalDateTime.now(),currentPlayer,false));
				var likedP = playerServiceImpl.findPlayerById(playerId);
				logger.info("{}({}) handshaked {}({})",currentPlayer.getPlayerName(),currentPlayer.getTeamName(),likedP.getPlayerName(),likedP.getTeamName());
				model.addFlashAttribute("msg","success");
			}else{
				var dbCollab = alreadyCollabed.get();
				dbCollab.setTime(LocalDateTime.now());
				likesRepo.save(dbCollab);
				model.addFlashAttribute("msg","warning");
				return "redirect:/squads/show-squad?selectedTeamName="+selectedTeamName+"&msg=warning";

			}
		}else{
			if(!alreadyCollabed.isPresent()){
				incrementLike(playerId);
				likesRepo.save(new Likes(playerId,LocalDateTime.now(),currentPlayer,true));
				var likedP = playerServiceImpl.findPlayerById(playerId);
				logger.info("{}({}) handshaked {}({})",currentPlayer.getPlayerName(),currentPlayer.getTeamName(),likedP.getPlayerName(),likedP.getTeamName());
				var dbCollab = likedPlayerAlreadyCollabed.get();
				dbCollab.setAccepted(true);
				likesRepo.save(dbCollab);

				model.addFlashAttribute("msg","success");
			}else{
				var dbCollab = alreadyCollabed.get();
				dbCollab.setTime(LocalDateTime.now());
				dbCollab.setAccepted(true);

				var likedPlayerDb = likedPlayerAlreadyCollabed.get();
				likedPlayerDb.setAccepted(true);

				likesRepo.save(dbCollab);
				likesRepo.save(likedPlayerDb);

				model.addFlashAttribute("msg","success");
			}
		}

		return "redirect:/squads/show-squad?selectedTeamName="+selectedTeamName+"&msg=success";
	}

	@GetMapping("/user/update-youtube")
	public String getYoutubeLinks(ModelMap map){
		if(youtubeService.getLinks().isPresent()){
			map.put("link",youtubeService.getLinks().get());
		}
		else{
			map.put("link",new YoutubeLinks());
		}
		return "youtubeLinks";
	}

	@PostMapping("/user/update-youtube")
	public String updateYoutubeLinks(ModelMap map,YoutubeLinks link){
		youtubeService.saveLinks(link);
		return "redirect:/fixtures";
	}

	@PostMapping("/user/upload-pdf")
	public String uploadPdf(@RequestParam("file") MultipartFile file){
		if(file.getOriginalFilename().contains(".pdf")){
			String user = TeamController.getCurrentUser();
			var player = playerServiceImpl.findPlayerByPhone(user).get();
			String pdfName = player.getTeam().getName()+".pdf";
			String message = "";
			try {
				storageService.save(file,pdfName);
				message = "Uploaded the file successfully: " + pdfName;
				return "redirect:/user-home";
			} catch (Exception e) {
				message = "Could not upload the file: " + pdfName + ". Error: " + e.getMessage();
				return "redirect:/user-home";
			}
		}else{
			return "redirect:/user-home";
		}
	}

	private void incrementLike(Long playerId) {
		var player = playerServiceImpl.findPlayerById(playerId);
		if(player.getLikes()!=null)
			player.setLikes(player.getLikes()+1);
		else
			player.setLikes(1);

		playerServiceImpl.savePlayerIgnoreImg(player);
	}

}
