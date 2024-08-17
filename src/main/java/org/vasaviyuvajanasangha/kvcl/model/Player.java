package org.vasaviyuvajanasangha.kvcl.model;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.web.multipart.MultipartFile;
import org.vasaviyuvajanasangha.kvcl.validator.ExtendedEmailValidator;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

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
	private String fatherName;
	
	private String fatherContact;
	
	@NotEmpty
	private String motherName;
	
	private String motherContact;
	
	@NotEmpty
	private String playerGothram;
	
	@NotEmpty
	private String playerPhone;
	
	@NotEmpty
	private String bloodGroup;
	
	@NotEmpty
	@Column(length = 1000)
	private String address;
	
	@Transient
	private MultipartFile photo;
	
	@Transient
	private String photoImg;
	
	@Lob
	private byte[] playerPhoto;
	
	private String jerseyNumber;
	
	@NotEmpty
	private String playerShirtSize;	
	
	private String profession;
	
	private int age;
	
	@NotEmpty
	private String playerSkill;
	
	@NotEmpty
	private String battingStyle;
	
	@NotEmpty
	private String bowlingStyle;
	
	private String teamName;
	
	private Boolean teamApproval;


	@Column(columnDefinition = "int4 default 0")
	private Integer likes;

	@OneToMany(mappedBy = "player")
	private List<Likes> likeList;
	
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "teamId",nullable = false)
	private Team team;
}
