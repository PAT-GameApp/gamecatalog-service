package com.cognizant.gamecatalog.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "game_catalog")
@EntityListeners(AuditingEntityListener.class)
public class Game_Catalog_Entity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "game_id")
    private Long game_id;

    @NotBlank(message = "Game name is required")
    @Column(name = "game_name", nullable = false)
    private String game_name;

    @Column(name = "game_locations")
    private String game_locations;

    @NotNull(message = "Number of players is required")
    @Positive(message = "Number of players must be positive")
    @Column(name = "game_num_players")
    private Integer game_numPlayers;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime created_at;

    @LastModifiedDate
    @Column(name = "modified_at")
    private LocalDateTime modified_at;
}