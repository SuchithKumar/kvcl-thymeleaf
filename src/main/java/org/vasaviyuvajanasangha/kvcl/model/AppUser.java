package org.vasaviyuvajanasangha.kvcl.model;

import org.vasaviyuvajanasangha.kvcl.validator.ExtendedEmailValidator;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AppUser {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long userId;
//	@Indexed(unique = true) // for unique field
	
	@NotNull
	private String username;
	
	@NotNull
	@Size(min = 8,message = "user password should be minimum 8 characters")
	private String password;
	
	@ExtendedEmailValidator
	private String email;
	
	@NotNull
	private String name;
	
	private String roles;

	@Transient
	@NotNull
	@Size(min = 8,message = "user password should be minimum 8 characters")
	private String reEnterPassword;
}