package com.cognizant.gamecatalog.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameResponse {
    private Long gameId;
    private String gameName;
    private String gameInfo;
    private LocationResponse location;
    private String gameFloor;
    private Integer numberOfPlayers;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
