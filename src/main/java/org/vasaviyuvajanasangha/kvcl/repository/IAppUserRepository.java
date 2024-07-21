package org.vasaviyuvajanasangha.kvcl.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.vasaviyuvajanasangha.kvcl.model.AppUser;


@Repository
public interface IAppUserRepository extends JpaRepository<AppUser, Long> {

	Optional<AppUser> findByUsername(String username);
	
}