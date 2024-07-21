package org.vasaviyuvajanasangha.kvcl.model;

import org.springframework.web.multipart.MultipartFile;
import org.vasaviyuvajanasangha.kvcl.validator.ExtendedEmailValidator;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Player {
	
	@Id
	@GeneratedValue
	private long playerId;
	
	@NotEmpty
	@ExtendedEmailValidator
	private String playerEmail;
	
	@NotEmpty
	private String playerName;
	
	@NotEmpty
	private String playerGothram;
	
	@NotEmpty
	private String playerPhone;
	
	@Transient
	private MultipartFile photo;
	
	@Transient
	private String photoImg;
	
	@Lob
	private byte[] playerPhoto;
	
	@NotEmpty
	private String jerseyNumber;
	
	@NotEmpty
	private String playerShirtSize;	
	
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "teamId",nullable = false)
	private Team team;
}
