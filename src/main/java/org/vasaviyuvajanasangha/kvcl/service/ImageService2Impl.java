package org.vasaviyuvajanasangha.kvcl.service;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Base64;

@Service
@Primary
public class ImageService2Impl implements ImageService{

    @Autowired
    private ResourceLoader resourceLoader;

    public String getImg(String name){
        String img = "";
        Resource resource = new ClassPathResource("static/images/"+name);

        String encodedfile=new String("data:image/png;base64,");
        try(InputStream inputStream = resource.getInputStream()) {
            byte[] arr = inputStream.readAllBytes();
            encodedfile = encodedfile.concat(Base64.getEncoder().encodeToString(arr));
            return encodedfile;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getImgSponsor(String name){
        String img = "";
        ClassPathResource resource = new ClassPathResource("static/images/sponsor-logo/"+name);

        String encodedfile=new String("data:image/png;base64,");
        try(InputStream inputStream = resource.getInputStream()) {
            byte[] arr = inputStream.readAllBytes();
            encodedfile = encodedfile.concat(Base64.getEncoder().encodeToString(arr));
            return encodedfile;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
