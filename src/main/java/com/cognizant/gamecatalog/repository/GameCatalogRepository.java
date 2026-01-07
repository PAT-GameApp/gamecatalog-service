package com.cognizant.gamecatalog.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cognizant.gamecatalog.entity.Game;

import java.util.List;

public interface GameCatalogRepository extends JpaRepository<Game, Long> {
    // Custom query methods can be added here

    // Example: Find games by location city
    // Example: Find games by location id
    @Query("SELECT g FROM Game g WHERE g.location.locationId = :locationId")
    List<Game> findByLocationLocationId(Long locationId);

    Page<Game> findByLocationLocationId(Long locationId, Pageable pageable);

    List<Game> findByLocationLocationId(Long locationId, Sort sort);
}
