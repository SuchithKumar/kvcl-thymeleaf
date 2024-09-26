package org.vasaviyuvajanasangha.kvcl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vasaviyuvajanasangha.kvcl.model.Editable;
import org.vasaviyuvajanasangha.kvcl.model.YoutubeLinks;

import java.util.Optional;

public interface YoutubeRepo extends JpaRepository<YoutubeLinks, Integer>{

}
