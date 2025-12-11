package com.cognizant.gamecatalog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cognizant.gamecatalog.entity.Game;

import java.util.List;

public interface GameCatalogRepository extends JpaRepository<Game, Long> {
    // Custom query methods can be added here

    // Example: Find games by location city
    @Query("SELECT g FROM Game g WHERE g.location.city = :city")
    List<Game> findByLocationCity(String city);
}
