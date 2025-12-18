package com.cognizant.gamecatalog.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.cognizant.gamecatalog.dto.GameRequest;
import com.cognizant.gamecatalog.dto.GameResponse;
import com.cognizant.gamecatalog.dto.LocationResponse;
import com.cognizant.gamecatalog.service.GameCatalogService;

@ExtendWith(MockitoExtension.class)
class GameCatalogControllerTest {

    @Mock
    private GameCatalogService gameCatalogService;

    @InjectMocks
    private GameCatalogController gameCatalogController;

    @Test
    void createGame_shouldReturnCreated() {
        GameRequest request = GameRequest.builder()
                .gameName("Chess")
                .gameInfo("Board game")
                .locationId(1L)
                .gameFloor("1st")
                .numberOfPlayers(2)
                .build();

        LocationResponse location = LocationResponse.builder()
                .locationId(1L)
                .country("Country")
                .city("City")
                .office("Office")
                .build();

        GameResponse responseDto = GameResponse.builder()
                .gameId(1L)
                .gameName("Chess")
                .gameInfo("Board game")
                .location(location)
                .gameFloor("1st")
                .numberOfPlayers(2)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();

        when(gameCatalogService.createGame(any(GameRequest.class))).thenReturn(responseDto);

        ResponseEntity<GameResponse> response = gameCatalogController.createGame(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getGameId());
    }

    @Test
    void getAllGames_shouldReturnOkAndList() {
        GameResponse g1 = GameResponse.builder().gameId(1L).build();
        GameResponse g2 = GameResponse.builder().gameId(2L).build();
        List<GameResponse> list = Arrays.asList(g1, g2);

        when(gameCatalogService.getAllGames()).thenReturn(list);

        ResponseEntity<List<GameResponse>> response = gameCatalogController.getAllGames();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void getGameById_found_shouldReturnOk() {
        Long id = 1L;
        GameResponse game = GameResponse.builder().gameId(id).build();

        when(gameCatalogService.getGameById(id)).thenReturn(game);

        ResponseEntity<GameResponse> response = gameCatalogController.getGameById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(id, response.getBody().getGameId());
    }

    @Test
    void getGameById_notFound_shouldReturn404() {
        Long id = 1L;
        when(gameCatalogService.getGameById(id)).thenReturn(null);

        ResponseEntity<GameResponse> response = gameCatalogController.getGameById(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void updateGame_found_shouldReturnOk() {
        Long id = 1L;
        GameRequest request = GameRequest.builder()
                .gameName("Updated")
                .locationId(1L)
                .numberOfPlayers(4)
                .build();

        GameResponse updated = GameResponse.builder().gameId(id).gameName("Updated").build();

        when(gameCatalogService.updateGame(id, request)).thenReturn(updated);

        ResponseEntity<GameResponse> response = gameCatalogController.updateGame(id, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Updated", response.getBody().getGameName());
    }

    @Test
    void updateGame_notFound_shouldReturn404() {
        Long id = 1L;
        GameRequest request = GameRequest.builder()
                .gameName("Updated")
                .locationId(1L)
                .numberOfPlayers(4)
                .build();

        when(gameCatalogService.updateGame(id, request)).thenReturn(null);

        ResponseEntity<GameResponse> response = gameCatalogController.updateGame(id, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteGame_shouldReturnOk() {
        Long id = 1L;

        ResponseEntity<String> response = gameCatalogController.deleteGame(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Game deleted", response.getBody());
    }

    @Test
    void getGamesByLocation_shouldReturnOk() {
        Long locationId = 1L;
        GameResponse g1 = GameResponse.builder().gameId(1L).build();
        List<GameResponse> list = Arrays.asList(g1);

        when(gameCatalogService.getGamesByLocation(locationId)).thenReturn(list);

        ResponseEntity<List<GameResponse>> response = gameCatalogController.getGamesByLocation(locationId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }
}
