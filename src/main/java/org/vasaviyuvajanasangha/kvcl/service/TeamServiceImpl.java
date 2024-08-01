package org.vasaviyuvajanasangha.kvcl.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.vasaviyuvajanasangha.kvcl.model.Player;
import org.vasaviyuvajanasangha.kvcl.model.Team;
import org.vasaviyuvajanasangha.kvcl.repository.TeamRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class TeamServiceImpl {

	@Autowired
	private TeamRepository repository;
	
	public Team saveTeamDetails(Team team) {
		
		try {
			if(team.getLogo()!=null) {
					MultipartFile file = team.getLogo();
//					String filename = team.getLogo().getOriginalFilename();
//					String ext = filename.substring(filename.lastIndexOf(".") + 1);
//					byte[] bytes = ImageFilter.resize(file, 450, ext);
			    	team.setTeamLogo(file.getBytes());
			}
			
			if(team.getPaymentInfoFile()!=null)
				team.setPaymentInfoDB(team.getPaymentInfoFile().getBytes());

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return repository.save(team);
	}
	
	public Optional<Team> findTeamByRegisterUser(String phone) {
		var team = repository.findByRegisteredUser(phone); 
		
		if(team.isEmpty()) {
			return team;
		}
		
		var updatedTeam = team.get();
		updatedTeam.setLogoImg("data:image/png;base64,"+Base64.getEncoder().encodeToString(updatedTeam.getTeamLogo()));
		
		if(updatedTeam.getPaymentInfoDB()!=null)
			updatedTeam.setPaymentInfoImg("data:image/png;base64,"+Base64.getEncoder().encodeToString(updatedTeam.getPaymentInfoDB()));

		
		var updatedPlayers = updatedTeam.getPlayers();
		if(!updatedTeam.getPlayers().isEmpty()) {
			for(Player player : updatedPlayers)
				player.setPhotoImg("data:image/png;base64,"+Base64.getEncoder().encodeToString(player.getPlayerPhoto()));
		}
		updatedTeam.setPlayers(updatedPlayers);
		return Optional.of(updatedTeam);
	}
	
	public Optional<Team> findTeamByName(String name) {
		var team = repository.findByName(name); 
		
		if(team.isEmpty()) {
			return team;
		}
		
		var updatedTeam = team.get();
		updatedTeam.setLogoImg("data:image/png;base64,"+Base64.getEncoder().encodeToString(updatedTeam.getTeamLogo()));
		
		if(updatedTeam.getPaymentInfoDB()!=null)
			updatedTeam.setPaymentInfoImg("data:image/png;base64,"+Base64.getEncoder().encodeToString(updatedTeam.getPaymentInfoDB()));

		
		var updatedPlayers = updatedTeam.getPlayers();
		if(!updatedTeam.getPlayers().isEmpty()) {
			for(Player player : updatedPlayers)
				player.setPhotoImg("data:image/png;base64,"+Base64.getEncoder().encodeToString(player.getPlayerPhoto()));
		}
		updatedTeam.setPlayers(updatedPlayers);
		return Optional.of(updatedTeam);
	}
	
	
	public List<Team> findAllTeams(){
		
		List<Team> newTeams = new ArrayList<>();
		for(Team team : repository.findAll()){
			
			var updatedTeam = team;
			updatedTeam.setLogoImg("data:image/png;base64,"+Base64.getEncoder().encodeToString(updatedTeam.getTeamLogo()));
			
			if(updatedTeam.getPaymentInfoDB()!=null)
				updatedTeam.setPaymentInfoImg("data:image/png;base64,"+Base64.getEncoder().encodeToString(updatedTeam.getPaymentInfoDB()));

			
			var updatedPlayers = updatedTeam.getPlayers();
			if(!updatedTeam.getPlayers().isEmpty()) {
				for(Player player : updatedPlayers)
					player.setPhotoImg("data:image/png;base64,"+Base64.getEncoder().encodeToString(player.getPlayerPhoto()));
			}
			updatedTeam.setPlayers(updatedPlayers);
			
			newTeams.add(updatedTeam);			
		}
		
		return newTeams;
	}
	
	
}
