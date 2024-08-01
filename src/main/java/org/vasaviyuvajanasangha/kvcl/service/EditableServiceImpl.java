package org.vasaviyuvajanasangha.kvcl.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vasaviyuvajanasangha.kvcl.model.Editable;
import org.vasaviyuvajanasangha.kvcl.repository.EditableRepo;

@Service
public class EditableServiceImpl {

	@Autowired
	private EditableRepo repo;
	
	
	public Editable getLatestUpdate() {
		Optional<Editable> editable = repo.findTopByOrderByUpdateDateDesc();
		if(editable.isEmpty()) {
			repo.save(new Editable(true,true,true,true,"7019393408"));
			return repo.findTopByOrderByUpdateDateDesc().get();
		}
					
		return repo.findTopByOrderByUpdateDateDesc().get();
	}
	
	public Editable save(Editable ann) {
		return repo.save(ann);
	}
	
}
