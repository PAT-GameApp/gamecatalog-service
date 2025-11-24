package com.cognizant.gamecatalog.serviceImpl;

import com.cognizant.gamecatalog.entity.Game_Catalog_Entity;
import com.cognizant.gamecatalog.service.Game_Catalog_Service;
import com.cognizant.gamecatalog.repository.Game_Catalog_Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class Game_Catalog_ServiceImpl implements Game_Catalog_Service {
    @Autowired
    private Game_Catalog_Repository gameCatalogRepository;

    @Override
    public Game_Catalog_Entity createGame(Game_Catalog_Entity game) {
        return gameCatalogRepository.save(game);
    }

    @Override
    public List<Game_Catalog_Entity> getAllGames() {
        return gameCatalogRepository.findAll();
    }

    @Override
    public Game_Catalog_Entity getGameById(Long id) {
        Optional<Game_Catalog_Entity> game = gameCatalogRepository.findById(id);
        return game.orElse(null);
    }

    @Override
    public Game_Catalog_Entity updateGame(Long id, Game_Catalog_Entity updatedGame) {
        if (gameCatalogRepository.existsById(id)) {
            updatedGame.setGame_id(id);
            return gameCatalogRepository.save(updatedGame);
        }
        return null;
    }

    @Override
    public void deleteGame(Long id) {
        gameCatalogRepository.deleteById(id);
    }
}
