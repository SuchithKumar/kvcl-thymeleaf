package org.vasaviyuvajanasangha.kvcl.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Likes {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long likedPlayerId;

    private LocalDateTime time;

    private boolean accepted;

    @ManyToOne
    @JoinColumn(name = "playerId",nullable = false)
    private Player player;

    public Likes(Long likedPlayerId,LocalDateTime time,Player player,boolean accepted){
        this.likedPlayerId = likedPlayerId;
        this.time = time;
        this.player = player;
        this.accepted = accepted;
    }
}
