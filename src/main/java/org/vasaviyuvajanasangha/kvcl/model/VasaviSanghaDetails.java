package org.vasaviyuvajanasangha.kvcl.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VasaviSanghaDetails {
	
	@Id
	@GeneratedValue
	private Long id;
	
	@NotNull
	private String city;
	
	@NotNull
	private String state;
	
	@NotEmpty
	private String vasaviSanghaName;
	
	@NotEmpty
	private String vasaviSanghaArea;
	
	@NotEmpty
	private String vsPresident;
	
	@NotEmpty
	private String vsPresidentNumber;
	
	@NotEmpty
	private String vsSecretary;
	
	@NotEmpty
	private String vsSecretaryNumber;
	
	@NotEmpty
	private String vsTreasurer;
	
	@NotEmpty
	private String vsTreasurerNumber;
	
		
	@OneToOne
	@JoinColumn(name = "teamId",nullable = false)
	private Team team;

	
}
