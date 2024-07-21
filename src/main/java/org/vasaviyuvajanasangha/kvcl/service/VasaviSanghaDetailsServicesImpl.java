package org.vasaviyuvajanasangha.kvcl.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vasaviyuvajanasangha.kvcl.model.VasaviSanghaDetails;
import org.vasaviyuvajanasangha.kvcl.repository.VsSanghaRepository;

@Service
public class VasaviSanghaDetailsServicesImpl {

	@Autowired
	private VsSanghaRepository vasaviSanghaDetailsRepo;
	
	
	public VasaviSanghaDetails saveVsDetails(VasaviSanghaDetails vsDetails) {
		return vasaviSanghaDetailsRepo.save(vsDetails);
	}
	
	
	
}
