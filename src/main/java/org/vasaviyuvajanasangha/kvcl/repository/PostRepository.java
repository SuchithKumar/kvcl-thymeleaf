package org.vasaviyuvajanasangha.kvcl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vasaviyuvajanasangha.kvcl.model.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
}
