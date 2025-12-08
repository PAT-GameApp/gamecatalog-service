package com.cognizant.gamecatalog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cognizant.gamecatalog.entity.Game;

import java.util.List;

public interface GameCatalogRepository extends JpaRepository<Game, Long> {
    // You can add custom query methods here if needed

    @Query("SELECT DISTINCT g.gameLocation FROM Game g WHERE g.gameLocation IS NOT NULL ORDER BY g.gameLocation")
    List<String> getLocations();

    @Query("SELECT g FROM Game g WHERE g.gameLocation = :gameLocations")
    List<Game> findByGameLocations(String gameLocations);
}
