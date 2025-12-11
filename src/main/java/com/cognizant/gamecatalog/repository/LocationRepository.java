package com.cognizant.gamecatalog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cognizant.gamecatalog.entity.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {
}
