package org.vasaviyuvajanasangha.kvcl.service;

import java.io.IOException;

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
	
	public Player findPlayerById(Long id) {
		return repository.findById(id).get();
	}
	
	public void deletePlayerById(Long id) {
		 var dbPlayer = repository.findById(id).get();
		 repository.delete(dbPlayer);
	}
	
	
}
