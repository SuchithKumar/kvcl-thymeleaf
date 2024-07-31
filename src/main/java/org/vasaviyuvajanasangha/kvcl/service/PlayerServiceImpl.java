package org.vasaviyuvajanasangha.kvcl.service;

import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.vasaviyuvajanasangha.kvcl.model.Player;
import org.vasaviyuvajanasangha.kvcl.repository.PlayerRepository;

@Controller
public class PlayerServiceImpl {

	@Autowired
	private PlayerRepository repository;
	
	public Player savePlayer(Player player) {
		try {
			player.setPlayerPhoto(player.getPhoto().getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return repository.save(player);
	}
	
	public Player savePlayerIgnoreImg(Player player) {
		var dbPlayer = repository.findById(player.getPlayerId());
		if(dbPlayer.isPresent())
			player.setPlayerPhoto(dbPlayer.get().getPlayerPhoto());
		else {
			try {
				player.setPlayerPhoto(player.getPhoto().getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return repository.save(player);
	}
	
	
	public Player findPlayerById(Long id) {
		var player = repository.findById(id).get();
		player.setPhotoImg("data:image/png;base64,"+Base64.getEncoder().encodeToString(player.getPlayerPhoto()));
		return player;
	}
	
	public void deletePlayerById(Long id) {
		 var dbPlayer = repository.findById(id).get();
		 repository.delete(dbPlayer);
	}
	
	public Optional<Player> findPlayerByPhone(String phone) {
		var optPlayer = repository.findByPlayerPhone(phone);
		
		if(optPlayer.isEmpty())
			return Optional.empty();
		
		var player = optPlayer.get();
		player.setPhotoImg("data:image/png;base64,"+Base64.getEncoder().encodeToString(player.getPlayerPhoto()));
		return Optional.of(player);
	}
}
