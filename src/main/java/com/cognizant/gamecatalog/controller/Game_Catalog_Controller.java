package com.cognizant.gamecatalog.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.gamecatalog.entity.Game_Catalog_Entity;
import com.cognizant.gamecatalog.service.Game_Catalog_Service;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/games")
@CrossOrigin(origins = "http://localhost:5173")
public class Game_Catalog_Controller {
    @Autowired
    private Game_Catalog_Service gameCatalogService;

    @PostMapping
    public ResponseEntity<String> createGame(@Valid @RequestBody Game_Catalog_Entity game) {
        gameCatalogService.createGame(game);
        return ResponseEntity.status(HttpStatus.CREATED).body("Game created");
    }

    @GetMapping
    public ResponseEntity<List<Game_Catalog_Entity>> getAllGames() {
        return ResponseEntity.ok(gameCatalogService.getAllGames());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Game_Catalog_Entity> getGameById(@PathVariable Long id) {
        Game_Catalog_Entity game = gameCatalogService.getGameById(id);
        if (game != null) {
            return ResponseEntity.ok(game);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Game_Catalog_Entity> updateGame(@PathVariable Long id,
            @Valid @RequestBody Game_Catalog_Entity game) {
        Game_Catalog_Entity updated = gameCatalogService.updateGame(id, game);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGame(@PathVariable Long id) {
        gameCatalogService.deleteGame(id);
        return ResponseEntity.status(HttpStatus.OK).body("Game deleted");
    }

    @GetMapping("/locations")
    public ResponseEntity<List<String>> getLocations() {
        return ResponseEntity.ok(gameCatalogService.getLocations());
    }

    @GetMapping("/locations/{location}")
    public ResponseEntity<List<Game_Catalog_Entity>> getGamesByLocation(@PathVariable String location) {
        return ResponseEntity.ok(gameCatalogService.getGamesByLocation(location));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + ex.getMessage());
    }
}
