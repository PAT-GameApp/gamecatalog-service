package com.cognizant.gamecatalog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.cognizant.gamecatalog.entity.Game_Catalog_Entity;
import java.util.List;

public interface Game_Catalog_Repository extends JpaRepository<Game_Catalog_Entity, Long> {
	// You can add custom query methods here if needed

	@Query("SELECT DISTINCT g.game_locations FROM Game_Catalog_Entity g WHERE g.game_locations IS NOT NULL ORDER BY g.game_locations")
	List<String> getLocations();

	@Query("SELECT g FROM Game_Catalog_Entity g WHERE g.game_locations = :gameLocations")
	List<Game_Catalog_Entity> findByGameLocations(String gameLocations);
}
