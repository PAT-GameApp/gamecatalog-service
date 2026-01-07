package com.cognizant.gamecatalog.service;

import com.cognizant.gamecatalog.dto.GameRequest;
import com.cognizant.gamecatalog.dto.GameResponse;
import com.cognizant.gamecatalog.dto.LocationResponse;
import com.cognizant.gamecatalog.entity.Game;
import com.cognizant.gamecatalog.entity.Location;
import com.cognizant.gamecatalog.repository.GameCatalogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GameCatalogService {
    @Autowired
    private GameCatalogRepository gameCatalogRepository;

    @Autowired
    private LocationService locationService;

    public GameResponse createGame(GameRequest request) {
        Location location = locationService.getLocationEntityById(request.getLocationId());
        if (location == null) {
            throw new RuntimeException("Location not found with ID: " + request.getLocationId());
        }

        Game game = Game.builder()
                .gameName(request.getGameName())
                .gameInfo(request.getGameInfo())
                .location(location)
                .gameFloor(request.getGameFloor())
                .numberOfPlayers(request.getNumberOfPlayers())
                .build();

        Game saved = gameCatalogRepository.save(game);
        return mapToResponse(saved);
    }

    public List<GameResponse> getAllGames() {
        return gameCatalogRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public Page<GameResponse> getAllGames(Pageable pageable) {
        return gameCatalogRepository.findAll(pageable).map(this::mapToResponse);
    }

    public List<GameResponse> getAllGames(Sort sort) {
        List<Game> list = sort == null || sort.isUnsorted() ? gameCatalogRepository.findAll()
                : gameCatalogRepository.findAll(sort);
        return list.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public GameResponse getGameById(Long id) {
        return gameCatalogRepository.findById(id)
                .map(this::mapToResponse)
                .orElse(null);
    }

    public GameResponse updateGame(Long id, GameRequest request) {
        return gameCatalogRepository.findById(id).map(existing -> {
            Location location = locationService.getLocationEntityById(request.getLocationId());
            if (location != null) {
                existing.setLocation(location);
            }
            existing.setGameName(request.getGameName());
            existing.setGameInfo(request.getGameInfo());
            existing.setGameFloor(request.getGameFloor());
            existing.setNumberOfPlayers(request.getNumberOfPlayers());

            return mapToResponse(gameCatalogRepository.save(existing));
        }).orElse(null);
    }

    public void deleteGame(Long id) {
        gameCatalogRepository.deleteById(id);
    }

    public List<GameResponse> getGamesByLocation(Long locationId) {
        return gameCatalogRepository.findByLocationLocationId(locationId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public Page<GameResponse> getGamesByLocation(Long locationId, Pageable pageable) {
        return gameCatalogRepository.findByLocationLocationId(locationId, pageable).map(this::mapToResponse);
    }

    public List<GameResponse> getGamesByLocation(Long locationId, Sort sort) {
        List<Game> list = sort == null || sort.isUnsorted()
                ? gameCatalogRepository.findByLocationLocationId(locationId)
                : gameCatalogRepository.findByLocationLocationId(locationId, sort);
        return list.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public GameResponse mapToResponse(Game game) {
        return GameResponse.builder()
                .gameId(game.getGameId())
                .gameName(game.getGameName())
                .gameInfo(game.getGameInfo())
                .location(mapLocationToResponse(game.getLocation()))
                .gameFloor(game.getGameFloor())
                .numberOfPlayers(game.getNumberOfPlayers())
                .createdAt(game.getCreatedAt())
                .modifiedAt(game.getModifiedAt())
                .build();
    }

    public LocationResponse mapLocationToResponse(Location location) {
        if (location == null)
            return null;
        return LocationResponse.builder()
                .locationId(location.getLocationId())
                .country(location.getCountry())
                .city(location.getCity())
                .office(location.getOffice())
                .build();
    }
}
