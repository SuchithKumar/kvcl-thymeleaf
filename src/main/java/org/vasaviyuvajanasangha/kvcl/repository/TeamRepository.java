package org.vasaviyuvajanasangha.kvcl.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.vasaviyuvajanasangha.kvcl.model.Team;

import jakarta.transaction.Transactional;



@Transactional
public interface TeamRepository extends JpaRepository<Team, Long>{
	
	Optional<Team> findByRegisteredUser(String registeredUser);
	Optional<Team> findByName(String name);

	@Query("SELECT DISTINCT t.name from Team t")
	List<String> findDistinctTeamNames();

}
