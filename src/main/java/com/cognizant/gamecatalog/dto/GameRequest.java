package com.cognizant.gamecatalog.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameRequest {
    @NotNull(message = "Game name cannot be empty")
    private String gameName;

    private String gameInfo;

    @NotNull(message = "Location ID is required")
    private Long locationId;

    private String gameFloor;

    @NotNull(message = "Number of players is required")
    @Positive(message = "Number of players must be positive")
    private Integer numberOfPlayers;
}
