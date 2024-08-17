package org.vasaviyuvajanasangha.kvcl.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vasaviyuvajanasangha.kvcl.model.Collabs;
import org.vasaviyuvajanasangha.kvcl.model.Likes;
import org.vasaviyuvajanasangha.kvcl.model.Player;
import org.vasaviyuvajanasangha.kvcl.repository.LikesRepo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LikesService {

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
}
