package org.vasaviyuvajanasangha.kvcl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vasaviyuvajanasangha.kvcl.model.Likes;
import org.vasaviyuvajanasangha.kvcl.model.Player;

import java.util.Optional;

public interface LikesRepo extends JpaRepository<Likes, Long>{
    Optional<Likes> findByLikedPlayerIdAndPlayer(Long likedPlayerId, Player player);
}
