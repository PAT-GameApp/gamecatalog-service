package com.cognizant.gamecatalog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cognizant.gamecatalog.entity.Game_Catalog_Entity;

public interface Game_Catalog_Repository extends JpaRepository<Game_Catalog_Entity, Long> {
	// You can add custom query methods here if needed
}
