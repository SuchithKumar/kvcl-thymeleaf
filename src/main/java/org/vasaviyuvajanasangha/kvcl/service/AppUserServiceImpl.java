package org.vasaviyuvajanasangha.kvcl.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.vasaviyuvajanasangha.kvcl.model.AppUser;
import org.vasaviyuvajanasangha.kvcl.repository.IAppUserRepository;

@Service
public class AppUserServiceImpl {

	
	@Autowired
	private IAppUserRepository repository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public AppUser saveAppUser(AppUser user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRoles("USER");
		return repository.save(user);
	}
	
	
	public Optional<AppUser> getUserFromUserName(String username){
		return repository.findByUsername(username);
	}
	
}
