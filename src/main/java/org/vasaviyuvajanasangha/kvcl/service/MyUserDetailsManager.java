package org.vasaviyuvajanasangha.kvcl.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.vasaviyuvajanasangha.kvcl.model.AppUser;
import org.vasaviyuvajanasangha.kvcl.repository.IAppUserRepository;

@Service
public class MyUserDetailsManager implements UserDetailsManager {
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private IAppUserRepository appUserRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<AppUser> user = appUserRepository.findByUsername(username);
		if(user.isPresent()) {
			var userObj = user.get();
			return User.builder()
						.username(username)
						.password(userObj.getPassword())
						.roles(userObj.getRoles()).build();
		}else {
			logger.error("user not found -> {}",username);
			throw new UsernameNotFoundException(username+" not found!");
		}
	}

	@Override
	public void createUser(UserDetails user) {
		// TODO Auto-generated method stub
	}

	@Override
	public void updateUser(UserDetails user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteUser(String username) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void changePassword(String oldPassword, String newPassword) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean userExists(String username) {
		// TODO Auto-generated method stub
		return false;
	}

	
	
}
