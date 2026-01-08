package com.cognizant.gamecatalog.service;

// ...existing imports...
import com.cognizant.gamecatalog.dto.GameRequest;
import com.cognizant.gamecatalog.dto.GameResponse;
import com.cognizant.gamecatalog.dto.LocationResponse;
import com.cognizant.gamecatalog.entity.Game;
import com.cognizant.gamecatalog.entity.Location;
import com.cognizant.gamecatalog.repository.GameCatalogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameCatalogServiceTest {

    @Mock
    private GameCatalogRepository gameCatalogRepository;

    @Mock
    private LocationService locationService;

    @InjectMocks
    private GameCatalogService gameCatalogService;

    private Location location;
    private Game game;

    @BeforeEach
    void setUp() {
        location = Location.builder()
                .locationId(1L)
                .country("Country")
                .city("City")
                .office("Office")
                .build();

        game = Game.builder()
                .gameId(1L)
                .gameName("Chess")
                .gameInfo("Board game")
                .location(location)
                .gameFloor("1st Floor")
                .numberOfPlayers(2)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void createGame_whenLocationExists_persistsAndReturnsResponse() {
        GameRequest request = GameRequest.builder()
                .gameName("Chess")
                .gameInfo("Board game")
                .locationId(1L)
                .gameFloor("1st Floor")
                .numberOfPlayers(2)
                .build();

        when(locationService.getLocationEntityById(1L)).thenReturn(location);
        when(gameCatalogRepository.save(any(Game.class))).thenAnswer(invocation -> {
            Game g = invocation.getArgument(0);
            g.setGameId(1L);
            return g;
        });

        GameResponse response = gameCatalogService.createGame(request);

        assertNotNull(response);
        assertEquals(1L, response.getGameId());
        assertEquals("Chess", response.getGameName());
        assertEquals("Country", response.getLocation().getCountry());
        verify(locationService).getLocationEntityById(1L);
        verify(gameCatalogRepository).save(any(Game.class));
    }

    @Test
    void createGame_whenLocationMissing_throwsRuntimeException() {
        GameRequest request = GameRequest.builder()
                .gameName("Chess")
                .locationId(99L)
                .numberOfPlayers(2)
                .build();

        when(locationService.getLocationEntityById(99L)).thenReturn(null);

        assertThrows(RuntimeException.class, () -> gameCatalogService.createGame(request));
        verify(locationService).getLocationEntityById(99L);
        verify(gameCatalogRepository, never()).save(any());
    }

    @Test
    void getAllGames_withoutPaging_returnsMappedList() {
        when(gameCatalogRepository.findAll()).thenReturn(List.of(game));

        List<GameResponse> result = gameCatalogService.getAllGames();

        assertThat(result).hasSize(1);
        assertEquals("Chess", result.get(0).getGameName());
        verify(gameCatalogRepository).findAll();
    }

    @Test
    void getAllGames_withPageable_delegatesToRepository() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<Game> page = new PageImpl<>(List.of(game), pageable, 1);
        when(gameCatalogRepository.findAll(pageable)).thenReturn(page);

        Page<GameResponse> result = gameCatalogService.getAllGames(pageable);

        assertThat(result.getContent()).hasSize(1);
        assertEquals("Chess", result.getContent().get(0).getGameName());
        verify(gameCatalogRepository).findAll(pageable);
    }

    @Test
    void getAllGames_withSorted_returnsSortedFromRepository() {
        Sort sort = Sort.by("gameName");
        when(gameCatalogRepository.findAll(sort)).thenReturn(List.of(game));

        List<GameResponse> result = gameCatalogService.getAllGames(sort);

        assertThat(result).containsExactly(gameCatalogService.mapToResponse(game));
        verify(gameCatalogRepository).findAll(sort);
    }

    @Test
    void getAllGames_withUnsortedSort_fallsBackToFindAll() {
        Sort sort = Sort.unsorted();
        when(gameCatalogRepository.findAll()).thenReturn(List.of(game));

        List<GameResponse> result = gameCatalogService.getAllGames(sort);

        assertThat(result).hasSize(1);
        verify(gameCatalogRepository).findAll();
        verify(gameCatalogRepository, never()).findAll(sort);
    }

    @Test
    void getGameById_whenFound_returnsResponse() {
        when(gameCatalogRepository.findById(1L)).thenReturn(Optional.of(game));

        GameResponse response = gameCatalogService.getGameById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getGameId());
        verify(gameCatalogRepository).findById(1L);
    }

    @Test
    void getGameById_whenNotFound_returnsNull() {
        when(gameCatalogRepository.findById(1L)).thenReturn(Optional.empty());

        GameResponse response = gameCatalogService.getGameById(1L);

        assertNull(response);
        verify(gameCatalogRepository).findById(1L);
    }

    @Test
    void updateGame_whenFound_updatesFieldsAndSaves() {
        GameRequest request = GameRequest.builder()
                .gameName("Updated")
                .gameInfo("Updated info")
                .locationId(2L)
                .gameFloor("2nd Floor")
                .numberOfPlayers(4)
                .build();

        Location newLocation = Location.builder()
                .locationId(2L)
                .country("NewCountry")
                .city("NewCity")
                .office("NewOffice")
                .build();

        when(gameCatalogRepository.findById(1L)).thenReturn(Optional.of(game));
        when(locationService.getLocationEntityById(2L)).thenReturn(newLocation);
        when(gameCatalogRepository.save(any(Game.class))).thenAnswer(invocation -> invocation.getArgument(0));

        GameResponse response = gameCatalogService.updateGame(1L, request);

        assertNotNull(response);
        assertEquals("Updated", response.getGameName());
        assertEquals("NewCountry", response.getLocation().getCountry());
        verify(gameCatalogRepository).findById(1L);
        verify(locationService).getLocationEntityById(2L);
        verify(gameCatalogRepository).save(any(Game.class));
    }

    @Test
    void updateGame_whenNotFound_returnsNull() {
        GameRequest request = GameRequest.builder()
                .gameName("Updated")
                .locationId(1L)
                .numberOfPlayers(2)
                .build();

        when(gameCatalogRepository.findById(1L)).thenReturn(Optional.empty());

        GameResponse response = gameCatalogService.updateGame(1L, request);

        assertNull(response);
        verify(gameCatalogRepository).findById(1L);
        verify(locationService, never()).getLocationEntityById(anyLong());
        verify(gameCatalogRepository, never()).save(any());
    }

    @Test
    void deleteGame_delegatesToRepository() {
        gameCatalogService.deleteGame(1L);
        verify(gameCatalogRepository).deleteById(1L);
    }

    @Test
    void getGamesByLocation_withoutPaging_returnsMappedList() {
        when(gameCatalogRepository.findByLocationLocationId(1L)).thenReturn(List.of(game));

        List<GameResponse> result = gameCatalogService.getGamesByLocation(1L);

        assertThat(result).hasSize(1);
        assertEquals(1L, result.get(0).getGameId());
        verify(gameCatalogRepository).findByLocationLocationId(1L);
    }

    @Test
    void getGamesByLocation_withPageable_delegatesToRepository() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<Game> page = new PageImpl<>(List.of(game), pageable, 1);
        when(gameCatalogRepository.findByLocationLocationId(1L, pageable)).thenReturn(page);

        Page<GameResponse> result = gameCatalogService.getGamesByLocation(1L, pageable);

        assertThat(result.getContent()).hasSize(1);
        verify(gameCatalogRepository).findByLocationLocationId(1L, pageable);
    }

    @Test
    void getGamesByLocation_withSorted_returnsSortedFromRepository() {
        Sort sort = Sort.by("gameName");
        when(gameCatalogRepository.findByLocationLocationId(1L, sort)).thenReturn(List.of(game));

        List<GameResponse> result = gameCatalogService.getGamesByLocation(1L, sort);

        assertThat(result).hasSize(1);
        verify(gameCatalogRepository).findByLocationLocationId(1L, sort);
    }

    @Test
    void getGamesByLocation_withUnsortedSort_fallsBackToFindByLocationId() {
        Sort sort = Sort.unsorted();
        when(gameCatalogRepository.findByLocationLocationId(1L)).thenReturn(List.of(game));

        List<GameResponse> result = gameCatalogService.getGamesByLocation(1L, sort);

        assertThat(result).hasSize(1);
        verify(gameCatalogRepository).findByLocationLocationId(1L);
        verify(gameCatalogRepository, never()).findByLocationLocationId(1L, sort);
    }

    @Test
    void mapToResponse_mapsAllFields() {
        GameResponse response = gameCatalogService.mapToResponse(game);

        assertEquals(game.getGameId(), response.getGameId());
        assertEquals(game.getGameName(), response.getGameName());
        assertEquals(game.getGameInfo(), response.getGameInfo());
        assertNotNull(response.getLocation());
        assertEquals(game.getLocation().getCountry(), response.getLocation().getCountry());
        assertEquals(game.getGameFloor(), response.getGameFloor());
        assertEquals(game.getNumberOfPlayers(), response.getNumberOfPlayers());
        assertEquals(game.getCreatedAt(), response.getCreatedAt());
        assertEquals(game.getModifiedAt(), response.getModifiedAt());
    }

    @Test
    void mapLocationToResponse_whenNull_returnsNull() {
        LocationResponse response = gameCatalogService.mapLocationToResponse(null);
        assertNull(response);
    }

    @Test
    void mapLocationToResponse_whenNotNull_mapsFields() {
        LocationResponse response = gameCatalogService.mapLocationToResponse(location);

        assertNotNull(response);
        assertEquals(location.getLocationId(), response.getLocationId());
        assertEquals(location.getCountry(), response.getCountry());
        assertEquals(location.getCity(), response.getCity());
        assertEquals(location.getOffice(), response.getOffice());
    }
}
