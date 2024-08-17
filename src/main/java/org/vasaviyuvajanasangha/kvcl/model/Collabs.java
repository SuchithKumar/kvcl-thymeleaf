package org.vasaviyuvajanasangha.kvcl.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Collabs {
    private String playerName;
    private Integer requestedCount;
    private Integer acceptedCount;
}
