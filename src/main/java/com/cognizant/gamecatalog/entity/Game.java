package com.cognizant.gamecatalog.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;

@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
@Builder
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gameId;

    @NotNull(message = "Game name cannot be empty")
    private String gameName;

    private String gameInfo;

    @NotNull(message = "Game location cannot be empty")
    private String gameLocation;

    private String gameFloor;

    @NotNull(message = "Number of players is required")
    @Positive(message = "Number of players must be positive")
    private Integer numberOfPlayers;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;
}
