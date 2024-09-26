package org.vasaviyuvajanasangha.kvcl;

import jakarta.annotation.Resource;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.vasaviyuvajanasangha.kvcl.pdf.upload.service.FilesStorageService;

@SpringBootApplication
public class KvlcSecureApplication implements CommandLineRunner {

	@Resource
	FilesStorageService storageService;

	public static void main(String[] args) {
		SpringApplication.run(KvlcSecureApplication.class, args);
	}

	@Override
	public void run(String... arg) throws Exception {
//    storageService.deleteAll();
		storageService.init();
	}
}
