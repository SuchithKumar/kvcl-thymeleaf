package org.vasaviyuvajanasangha.kvcl.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vasaviyuvajanasangha.kvcl.controller.TeamController;
import org.vasaviyuvajanasangha.kvcl.model.Post;
import org.vasaviyuvajanasangha.kvcl.repository.PostRepository;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PlayerServiceImpl playerService;

    @Override
    public Post addPost(Post post) {
        var user = playerService.findPlayerByPhone(TeamController.getCurrentUser()).get();
        String updatedBy = user.getPlayerName()+" ("+user.getTeamName()+")";
        LocalDateTime time = LocalDateTime.now();
        try {
            if(!post.getPhoto().isEmpty())
                post.setPostPhoto(PlayerServiceImpl.getResizedImage(post.getPhoto().getBytes()));
            post.setTimePosted(time);
            post.setPostedBy(updatedBy);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return postRepository.save(post);
    }

    @Override
    public void removePost(Post post) {
        postRepository.delete(post);
    }

    @Override
    public Post findPostByID(Long id) {
        return postRepository.findById(id).get();
    }

    @Override
    public List<Post> retrievePosts() {
        List<Post> posts = postRepository.findAll();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM HH:mm a");
        posts = posts.stream().sorted(Comparator.comparing(Post::getTimePosted).reversed()).collect(Collectors.toList());
        posts.stream().forEach(a -> {
            if(a.getPostPhoto().length!=0){
                a.setPostImg("data:image/png;base64,"+ Base64.getEncoder().encodeToString(a.getPostPhoto()));
            }
            a.setPostedDate(a.getTimePosted().format(formatter));
        });

        return posts;
    }
}
