package org.vasaviyuvajanasangha.kvcl.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vasaviyuvajanasangha.kvcl.model.YoutubeLinks;
import org.vasaviyuvajanasangha.kvcl.repository.YoutubeRepo;

import java.util.Optional;

@Service
public class YoutubeService {

    @Autowired
    private YoutubeRepo youtubeRepo;

    public void saveLinks(YoutubeLinks links){
        if(getLinks().isPresent()){
            var dbLinks = getLinks().get();
            dbLinks.setDay1APS(links.getDay1APS());
            dbLinks.setDay2APS(links.getDay2APS());
            dbLinks.setDay3APS(links.getDay3APS());
            dbLinks.setDay1NCB(links.getDay1NCB());
            dbLinks.setDay2NCB(links.getDay2NCB());
            dbLinks.setDay1NCJ(links.getDay1NCJ());
            youtubeRepo.save(dbLinks);
        }else{
            youtubeRepo.save(links);
        }
    }

    public Optional<YoutubeLinks> getLinks(){
       var links = youtubeRepo.findAll();
       if(links.size()==0){
           return Optional.empty();
       }else{
           return Optional.of(links.get(0));
       }
    }

}
