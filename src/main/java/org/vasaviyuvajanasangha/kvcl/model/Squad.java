package org.vasaviyuvajanasangha.kvcl.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Squad {

    private Team team;
    private Player teamCaptain;
    private List<Player> approvedPlayers;

}
