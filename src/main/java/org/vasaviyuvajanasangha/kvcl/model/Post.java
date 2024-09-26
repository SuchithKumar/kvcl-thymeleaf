package org.vasaviyuvajanasangha.kvcl.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String text;
    @Transient
    private MultipartFile photo;
    @Transient
    private String postImg;
    @Lob
    private byte[] postPhoto;
    private LocalDateTime timePosted;
    private String postedBy;

    private String embed;

    @Transient
    private String postedDate;

}
