package org.vasaviyuvajanasangha.kvcl.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class YoutubeLinks {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String day1NCB;

    private String day1NCJ;

    private String day1APS;

    private String day2NCB;

    private String day2APS;

    private String day3APS;
}
