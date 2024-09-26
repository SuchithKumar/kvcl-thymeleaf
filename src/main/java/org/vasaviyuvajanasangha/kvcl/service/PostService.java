package org.vasaviyuvajanasangha.kvcl.service;

import org.vasaviyuvajanasangha.kvcl.model.Post;

import java.util.List;

public interface PostService {
    Post addPost(Post post);
    void removePost(Post post);

    Post findPostByID(Long id);
    List<Post> retrievePosts();

}
