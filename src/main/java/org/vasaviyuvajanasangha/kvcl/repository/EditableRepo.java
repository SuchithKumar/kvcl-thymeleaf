package org.vasaviyuvajanasangha.kvcl.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vasaviyuvajanasangha.kvcl.model.Editable;

public interface EditableRepo extends JpaRepository<Editable, Integer>{
	
	Optional<Editable> findTopByOrderByUpdateDateDesc();

}
