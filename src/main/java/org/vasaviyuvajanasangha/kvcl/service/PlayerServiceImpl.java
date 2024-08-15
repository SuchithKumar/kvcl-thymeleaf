package org.vasaviyuvajanasangha.kvcl.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.vasaviyuvajanasangha.kvcl.model.ApprovalTable;
import org.vasaviyuvajanasangha.kvcl.model.Player;
import org.vasaviyuvajanasangha.kvcl.repository.PlayerRepository;
import org.vasaviyuvajanasangha.kvcl.repository.PlayerRepository.NameOnly;
import org.vasaviyuvajanasangha.kvcl.utils.EXIFUtilities;
import org.vasaviyuvajanasangha.kvcl.utils.ImageResizer;

import javax.imageio.ImageIO;

@Controller
public class PlayerServiceImpl {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PlayerRepository repository;
	
	public Player savePlayer(Player player) {
		try {
			player.setPlayerPhoto(getResizedImage(player.getPhoto().getBytes()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return repository.save(player);
	}

	public Player updatePlayer(Player player) {
		player.setPlayerPhoto(getResizedImage(player.getPlayerPhoto()));
		return repository.save(player);
	}

	private byte[] getResizedImage(byte[] input){
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(input);
			BufferedImage bufferedImage = null;
			var imageIo = EXIFUtilities.readWithOrientation(bis);
			if(imageIo==null){
				bufferedImage = ImageIO.read(bis);
			}else {
				if(imageIo.getRenderedImage()!=null)
					bufferedImage =	(BufferedImage) imageIo.getRenderedImage();
				else
					bufferedImage = ImageIO.read(bis);
			}
//		BufferedImage bufferedImage = ImageIO.read(bis);
			bufferedImage = ImageResizer.resizeImage(bufferedImage, 780, 900);
			File file = new File("outputImage.jpg");
			ImageIO.setUseCache(false);
			ImageIO.write(bufferedImage, "jpg", file);

			return Files.readAllBytes(file.toPath());
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return input;
	}

	public Player saveResizedImage(Player player){
		logger.info("resizing image of {} {}",player.getPlayerName(),player.getTeamName());
		if(player.getPlayerPhoto()!=null) {
			try {
				ByteArrayInputStream bis = new ByteArrayInputStream(player.getPlayerPhoto());
				BufferedImage bufferedImage = null;
				var imageIo = EXIFUtilities.readWithOrientation(bis);
				if(imageIo==null){
					bufferedImage = ImageIO.read(bis);
				}else {
					if(imageIo.getRenderedImage()!=null)
						bufferedImage =	(BufferedImage) imageIo.getRenderedImage();
					else
						bufferedImage = ImageIO.read(bis);
				}
//		BufferedImage bufferedImage = ImageIO.read(bis);
				bufferedImage = ImageResizer.resizeImage(bufferedImage, 780, 900);
				File file = new File("outputImage.jpg");
				ImageIO.setUseCache(false);
				ImageIO.write(bufferedImage, "jpg", file);

				player.setPlayerPhoto(Files.readAllBytes(file.toPath()));
				return repository.save(player);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
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
		if(player.getPlayerPhoto()!=null)
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
		if(player.getPlayerPhoto()!=null)
			player.setPhotoImg("data:image/png;base64,"+Base64.getEncoder().encodeToString(player.getPlayerPhoto()));
		return Optional.of(player);
	}
	
	public List<ApprovalTable> getTeamApprovalReport(){
		List<NameOnly> rep = repository.teamApprovalView();
		List<ApprovalTable> table = new ArrayList<>();
		rep.forEach(a-> table.add(new ApprovalTable(a.getTeamName(),a.getApproved(),a.getUnApproved())));
		return table;
	}

	public List<Player> findAllPlayers(){
		return repository.findAll();
	}
}
