package org.vasaviyuvajanasangha.kvcl.model;

import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Lob;
import jakarta.persistence.Transient;
import lombok.Data;

@Data
@Embeddable
public class FileEntity {
	
	@Transient
	private MultipartFile file;
	
	@Transient
	private String fileImg;
	
	@Lob
	private byte[] fileDB;
	
}
