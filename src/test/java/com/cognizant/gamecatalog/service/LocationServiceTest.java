package com.cognizant.gamecatalog.service;

// ...existing imports...
import com.cognizant.gamecatalog.dto.LocationRequest;
import com.cognizant.gamecatalog.dto.LocationResponse;
import com.cognizant.gamecatalog.entity.Location;
import com.cognizant.gamecatalog.repository.LocationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LocationServiceTest {

    @Mock
    private LocationRepository locationRepository;

    @InjectMocks
    private LocationService locationService;

    private Location location;

    @BeforeEach
    void setUp() {
        location = Location.builder()
                .locationId(1L)
                .country("Country")
                .city("City")
                .office("Office")
                .build();
    }

    @Test
    void createLocation_persistsAndReturnsResponse() {
        LocationRequest request = LocationRequest.builder()
                .country("Country")
                .city("City")
                .office("Office")
                .build();

        when(locationRepository.save(any(Location.class))).thenAnswer(invocation -> {
            Location l = invocation.getArgument(0);
            l.setLocationId(1L);
            return l;
        });

        LocationResponse response = locationService.createLocation(request);

        assertNotNull(response);
        assertEquals(1L, response.getLocationId());
        assertEquals("Country", response.getCountry());
        verify(locationRepository).save(any(Location.class));
    }

    @Test
    void getAllLocations_withoutPaging_returnsMappedList() {
        when(locationRepository.findAll()).thenReturn(List.of(location));

        List<LocationResponse> result = locationService.getAllLocations();

        assertThat(result).hasSize(1);
        assertEquals("Country", result.get(0).getCountry());
        verify(locationRepository).findAll();
    }

    @Test
    void getAllLocations_withPageable_delegatesToRepository() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<Location> page = new PageImpl<>(List.of(location), pageable, 1);
        when(locationRepository.findAll(pageable)).thenReturn(page);

        Page<LocationResponse> result = locationService.getAllLocations(pageable);

        assertThat(result.getContent()).hasSize(1);
        verify(locationRepository).findAll(pageable);
    }

    @Test
    void getAllLocations_withSorted_returnsSortedFromRepository() {
        Sort sort = Sort.by("country");
        when(locationRepository.findAll(sort)).thenReturn(List.of(location));

        List<LocationResponse> result = locationService.getAllLocations(sort);

        assertThat(result).hasSize(1);
        verify(locationRepository).findAll(sort);
    }

    @Test
    void getAllLocations_withUnsortedSort_fallsBackToFindAll() {
        Sort sort = Sort.unsorted();
        when(locationRepository.findAll()).thenReturn(List.of(location));

        List<LocationResponse> result = locationService.getAllLocations(sort);

        assertThat(result).hasSize(1);
        verify(locationRepository).findAll();
        verify(locationRepository, never()).findAll(sort);
    }

    @Test
    void getLocationById_whenFound_returnsResponse() {
        when(locationRepository.findById(1L)).thenReturn(Optional.of(location));

        LocationResponse response = locationService.getLocationById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getLocationId());
        verify(locationRepository).findById(1L);
    }

    @Test
    void getLocationById_whenNotFound_returnsNull() {
        when(locationRepository.findById(1L)).thenReturn(Optional.empty());

        LocationResponse response = locationService.getLocationById(1L);

        assertNull(response);
        verify(locationRepository).findById(1L);
    }

    @Test
    void getLocationEntityById_returnsEntityOrNull() {
        when(locationRepository.findById(1L)).thenReturn(Optional.of(location));

        Location result = locationService.getLocationEntityById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getLocationId());
        verify(locationRepository).findById(1L);

        when(locationRepository.findById(2L)).thenReturn(Optional.empty());
        assertNull(locationService.getLocationEntityById(2L));
        verify(locationRepository).findById(2L);
    }

    @Test
    void updateLocation_whenFound_updatesAndReturnsResponse() {
        LocationRequest request = LocationRequest.builder()
                .country("NewCountry")
                .city("NewCity")
                .office("NewOffice")
                .build();

        when(locationRepository.findById(1L)).thenReturn(Optional.of(location));
        when(locationRepository.save(any(Location.class))).thenAnswer(invocation -> invocation.getArgument(0));

        LocationResponse response = locationService.updateLocation(1L, request);

        assertNotNull(response);
        assertEquals("NewCountry", response.getCountry());
        assertEquals("NewCity", response.getCity());
        assertEquals("NewOffice", response.getOffice());
        verify(locationRepository).findById(1L);
        verify(locationRepository).save(any(Location.class));
    }

    @Test
    void updateLocation_whenNotFound_returnsNull() {
        LocationRequest request = LocationRequest.builder()
                .country("NewCountry")
                .city("NewCity")
                .office("NewOffice")
                .build();

        when(locationRepository.findById(1L)).thenReturn(Optional.empty());

        LocationResponse response = locationService.updateLocation(1L, request);

        assertNull(response);
        verify(locationRepository).findById(1L);
        verify(locationRepository, never()).save(any(Location.class));
    }

    @Test
    void deleteLocation_delegatesToRepository() {
        locationService.deleteLocation(1L);
        verify(locationRepository).deleteById(1L);
    }

    @Test
    void mapToResponse_mapsAllFields() {
        LocationResponse response = locationService.mapToResponse(location);

        assertEquals(location.getLocationId(), response.getLocationId());
        assertEquals(location.getCountry(), response.getCountry());
        assertEquals(location.getCity(), response.getCity());
        assertEquals(location.getOffice(), response.getOffice());
    }
}
