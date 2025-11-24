package com.cognizant.gamecatalog.service;

import com.cognizant.gamecatalog.entity.Game_Catalog_Entity;

public interface Game_Catalog_Service {

    Game_Catalog_Entity createGame(Game_Catalog_Entity game);
    java.util.List<Game_Catalog_Entity> getAllGames();
    Game_Catalog_Entity getGameById(Long id);
    Game_Catalog_Entity updateGame(Long id, Game_Catalog_Entity game);
    void deleteGame(Long id);

}
