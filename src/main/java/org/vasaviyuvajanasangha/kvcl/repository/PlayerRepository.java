package org.vasaviyuvajanasangha.kvcl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vasaviyuvajanasangha.kvcl.model.Player;

import jakarta.transaction.Transactional;

@Transactional
public interface PlayerRepository extends JpaRepository<Player, Long>{

}
