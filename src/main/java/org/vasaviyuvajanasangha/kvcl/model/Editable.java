package org.vasaviyuvajanasangha.kvcl.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class Editable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@NotNull
	private Boolean editTeamDetails;
	
	@NotNull
	private Boolean editVasaviMathaDetails;
	
	@NotNull
	private Boolean editPaymentDetails;
	
	@NotNull
	private Boolean editPlayers;
	
	@NotNull
	@PastOrPresent
	private LocalDateTime updateDate;
	
	@NotEmpty
	private String updatedBy;
	
	public Editable(Boolean a,Boolean b, Boolean c,Boolean d,String e) {
		this.editTeamDetails = a;
		this.editVasaviMathaDetails = b;
		this.editPaymentDetails = c;
		this.editPlayers = d;
		this.updateDate = LocalDateTime.now();
		this.updatedBy = e;
	}
	
}
