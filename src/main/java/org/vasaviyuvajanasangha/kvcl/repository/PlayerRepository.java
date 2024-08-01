package org.vasaviyuvajanasangha.kvcl.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.vasaviyuvajanasangha.kvcl.model.Player;

import jakarta.transaction.Transactional;


@Transactional
public interface PlayerRepository extends JpaRepository<Player, Long>{
	
	Optional<Player> findByPlayerPhone(String playerPhone);
	
	@Query(value = "select count(case when team_approval = true then 1 end) as \"approved\",count(case when team_approval = false then 1 end) as \"unapproved\", team_name as teamname from player group by team_name",nativeQuery = true)
	List<NameOnly> teamApprovalView();
	
	   public static interface NameOnly {

		     Integer getApproved();

		     Integer getUnApproved();

		     String getTeamName();
		  }
}
