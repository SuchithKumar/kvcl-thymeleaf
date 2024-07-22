package org.vasaviyuvajanasangha.kvcl.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Announcement {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotEmpty
	private String announcement;
	
	@NotEmpty
	private String madeBy;
	
	@PastOrPresent
	private LocalDateTime announcedDate;
	
	
	public Announcement(String announcement,String madeBy,LocalDateTime date) {
		this.announcement = announcement;
		this.madeBy = madeBy;
		this.announcedDate = date;
	}
	
}
