package com.cognizant.gamecatalog.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.cognizant.gamecatalog.dto.LocationRequest;
import com.cognizant.gamecatalog.dto.LocationResponse;
import com.cognizant.gamecatalog.service.LocationService;

@ExtendWith(MockitoExtension.class)
class LocationControllerTest {

    @Mock
    private LocationService locationService;

    @InjectMocks
    private LocationController locationController;

    @Test
    void getAllLocations_shouldReturnOkAndList() {
        LocationResponse l1 = LocationResponse.builder().locationId(1L).build();
        LocationResponse l2 = LocationResponse.builder().locationId(2L).build();
        List<LocationResponse> list = Arrays.asList(l1, l2);

        when(locationService.getAllLocations()).thenReturn(list);

        ResponseEntity<List<LocationResponse>> response = locationController.getAllLocations();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void getLocationById_found_shouldReturnOk() {
        Long id = 1L;
        LocationResponse loc = LocationResponse.builder().locationId(id).build();

        when(locationService.getLocationById(id)).thenReturn(loc);

        ResponseEntity<LocationResponse> response = locationController.getLocationById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(id, response.getBody().getLocationId());
    }

    @Test
    void getLocationById_notFound_shouldReturn404() {
        Long id = 1L;
        when(locationService.getLocationById(id)).thenReturn(null);

        ResponseEntity<LocationResponse> response = locationController.getLocationById(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void createLocation_shouldReturnCreated() {
        LocationRequest request = LocationRequest.builder()
                .country("Country")
                .city("City")
                .office("Office")
                .build();

        LocationResponse created = LocationResponse.builder()
                .locationId(1L)
                .country("Country")
                .city("City")
                .office("Office")
                .build();

        when(locationService.createLocation(any(LocationRequest.class))).thenReturn(created);

        ResponseEntity<LocationResponse> response = locationController.createLocation(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getLocationId());
    }

    @Test
    void updateLocation_found_shouldReturnOk() {
        Long id = 1L;
        LocationRequest request = LocationRequest.builder()
                .country("NewCountry")
                .city("NewCity")
                .office("NewOffice")
                .build();

        LocationResponse updated = LocationResponse.builder()
                .locationId(id)
                .country("NewCountry")
                .city("NewCity")
                .office("NewOffice")
                .build();

        when(locationService.updateLocation(id, request)).thenReturn(updated);

        ResponseEntity<LocationResponse> response = locationController.updateLocation(id, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("NewCountry", response.getBody().getCountry());
    }

    @Test
    void updateLocation_notFound_shouldReturn404() {
        Long id = 1L;
        LocationRequest request = LocationRequest.builder()
                .country("NewCountry")
                .city("NewCity")
                .office("NewOffice")
                .build();

        when(locationService.updateLocation(id, request)).thenReturn(null);

        ResponseEntity<LocationResponse> response = locationController.updateLocation(id, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteLocation_shouldReturnNoContent() {
        Long id = 1L;

        ResponseEntity<Void> response = locationController.deleteLocation(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
