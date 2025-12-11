package com.cognizant.gamecatalog.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.gamecatalog.dto.GameRequest;
import com.cognizant.gamecatalog.dto.GameResponse;
import com.cognizant.gamecatalog.service.GameCatalogService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/games")
public class GameCatalogController {
    @Autowired
    private GameCatalogService gameCatalogService;

    @PostMapping("/")
    public ResponseEntity<GameResponse> createGame(@Valid @RequestBody GameRequest gameRequest) {
        GameResponse createdGame = gameCatalogService.createGame(gameRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGame);
    }

    @GetMapping("/")
    public ResponseEntity<List<GameResponse>> getAllGames() {
        return ResponseEntity.ok(gameCatalogService.getAllGames());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GameResponse> getGameById(@PathVariable Long id) {
        GameResponse game = gameCatalogService.getGameById(id);
        if (game != null) {
            return ResponseEntity.ok(game);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<GameResponse> updateGame(@PathVariable Long id, @Valid @RequestBody GameRequest gameRequest) {
        GameResponse updated = gameCatalogService.updateGame(id, gameRequest);
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

    // Locations endpoint moved to LocationController

    @GetMapping("/locations/{location}")
    public ResponseEntity<List<GameResponse>> getGamesByLocation(@PathVariable String location) {
        return ResponseEntity.ok(gameCatalogService.getGamesByLocation(location));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + ex.getMessage());
    }
}
