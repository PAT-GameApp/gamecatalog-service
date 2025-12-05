package com.cognizant.gamecatalog.service;

import com.cognizant.gamecatalog.entity.Game;
import com.cognizant.gamecatalog.service.GameCatalogService;
import com.cognizant.gamecatalog.repository.GameCatalogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class GameCatalogService {
    @Autowired
    private GameCatalogRepository gameCatalogRepository;

    public Game createGame(Game game) {
        return gameCatalogRepository.save(game);
    }

    public List<Game> getAllGames() {
        return gameCatalogRepository.findAll();
    }

    public Game getGameById(Long id) {
        Optional<Game> game = gameCatalogRepository.findById(id);
        return game.orElse(null);
    }

    public Game updateGame(Long id, Game updatedGame) {
        if (gameCatalogRepository.existsById(id)) {
            return gameCatalogRepository.save(updatedGame);
        }
        return null;
    }

    public void deleteGame(Long id) {
        gameCatalogRepository.deleteById(id);
    }

    public List<String> getLocations() {
        return gameCatalogRepository.getLocations();
    }

    public List<Game> getGamesByLocation(String location) {
        return gameCatalogRepository.findByGameLocations(location);
    }
}
