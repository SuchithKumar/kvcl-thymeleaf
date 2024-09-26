package org.vasaviyuvajanasangha.kvcl.model;

import java.util.List;

import jakarta.persistence.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Team {

	@Id
	@GeneratedValue
	private long id;
	
	@NotEmpty
	private String name;
	
	@NotEmpty
	private String captain;
	
	@NotNull
	private String registeredUser;
	
	@Transient
	private MultipartFile logo;
	
	@Transient
	private String logoImg;
	
	@Lob
	private byte[] teamLogo;
	
	@NotEmpty
	private String requireAccomodation;
	
	private String noOfAccomodation;
	
	@NotEmpty
	private String requireFood;
	
	@Transient
	private MultipartFile paymentInfoFile;
	
	@Transient
	private String paymentInfoImg;
	
	@Lob
	private byte[] paymentInfoDB;

	private String sponsor;

	private String sponsorName;

	@Embedded
	private AccommodationDetails accommodationDetails;
	
	@OneToOne(mappedBy = "team",fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	private VasaviSanghaDetails vsDetails;
	
	
	@OneToMany(mappedBy = "team",fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	private List<Player> players;
	

}
