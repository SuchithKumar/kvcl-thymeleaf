package org.vasaviyuvajanasangha.kvcl.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Service;
import org.vasaviyuvajanasangha.kvcl.model.Collabs;
import org.vasaviyuvajanasangha.kvcl.model.Likes;
import org.vasaviyuvajanasangha.kvcl.model.Player;
import org.vasaviyuvajanasangha.kvcl.model.pojo.TeamPlayer;
import org.vasaviyuvajanasangha.kvcl.repository.LikesRepo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LikesService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private LikesRepo likesRepo;

    public Map<Player, Integer> getCollabCount(){
        var likes = likesRepo.findAll();
        Map<Player, Integer> countMap = new HashMap<>();
        for(Likes like : likes){
            Player firstKey = like.getPlayer();
            if(like.isAccepted()) {
                if (countMap.containsKey(firstKey)) {
                    int count = countMap.get(firstKey);
                    countMap.put(firstKey,++count);
                } else {
                    countMap.put(firstKey, 1);
                }
            }
        }
        return countMap;
    }

    public void deleteLikes(){
        likesRepo.deleteAll();
    }

    public List<TeamPlayer> fetchWhoLikedMe(Long playerId){
        return jdbcTemplate.query("select (select p2.player_name as handshaker from player p2 where p2.player_id = l.player_id),(select p2.team_name as handshaker_team from player p2 where p2.player_id = l.player_id) from likes l ,player p where l.liked_player_id = p.player_id and p.player_id=? order by 1",
                (rs,rowNum) -> new TeamPlayer(rs.getString(1),rs.getString(2)),
                playerId);
    }

    public List<TeamPlayer> fetchWhomIHaveLikedMe(Long playerId){
        return jdbcTemplate.query("select (select p2.player_name as handshakedTo from player p2 where p2.player_id = l.liked_player_id),(select p2.team_name as handshaker_team from player p2 where p2.player_id = l.liked_player_id) from likes l ,player p where l.player_id = p.player_id and p.player_id=? order by 1",
                (rs,rowNum) -> new TeamPlayer(rs.getString(1),rs.getString(2)),
                playerId);
    }
}
