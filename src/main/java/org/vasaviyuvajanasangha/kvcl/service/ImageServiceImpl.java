package org.vasaviyuvajanasangha.kvcl.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ResourceLoader resourceLoader;

    public String getImg(String name){
        String img = "";
        ClassPathResource resource = new ClassPathResource("static/images/"+name);
        File file = null;
        byte[] fileContent = null;
        String encodedfile=new String("data:image/png;base64,");
        try {
            file = resource.getFile();
            fileContent = Files.readAllBytes(file.toPath());
            encodedfile = encodedfile.concat(Base64.getEncoder().encodeToString(fileContent));
            return encodedfile;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getImgSponsor(String name){
        String img = "";
        ClassPathResource resource = new ClassPathResource("static/images/sponsor-logo/"+name);
        File file = null;
        byte[] fileContent = null;
        String encodedfile=new String("data:image/png;base64,");
        try {
            file = resource.getFile();
            fileContent = Files.readAllBytes(file.toPath());
            encodedfile = encodedfile.concat(Base64.getEncoder().encodeToString(fileContent));
            return encodedfile;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
