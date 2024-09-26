package org.vasaviyuvajanasangha.kvcl.model.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerWiseLikeCount {
    private String player;
    private String count;
}
