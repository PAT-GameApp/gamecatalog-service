package com.cognizant.gamecatalog.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cognizant.gamecatalog.dto.LocationRequest;
import com.cognizant.gamecatalog.dto.LocationResponse;
import com.cognizant.gamecatalog.entity.Location;
import com.cognizant.gamecatalog.repository.LocationRepository;

@Service
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;

    public LocationResponse createLocation(LocationRequest request) {
        Location location = Location.builder()
                .country(request.getCountry())
                .city(request.getCity())
                .office(request.getOffice())
                .build();
        Location saved = locationRepository.save(location);
        return mapToResponse(saved);
    }

    public List<LocationResponse> getAllLocations() {
        return locationRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public LocationResponse getLocationById(Long id) {
        return locationRepository.findById(id)
                .map(this::mapToResponse)
                .orElse(null);
    }

    public Location getLocationEntityById(Long id) {
        return locationRepository.findById(id).orElse(null);
    }

    public LocationResponse updateLocation(Long id, LocationRequest request) {
        return locationRepository.findById(id).map(existing -> {
            existing.setCountry(request.getCountry());
            existing.setCity(request.getCity());
            existing.setOffice(request.getOffice());
            return mapToResponse(locationRepository.save(existing));
        }).orElse(null);
    }

    public void deleteLocation(Long id) {
        locationRepository.deleteById(id);
    }

    private LocationResponse mapToResponse(Location location) {
        return LocationResponse.builder()
                .locationId(location.getLocationId())
                .country(location.getCountry())
                .city(location.getCity())
                .office(location.getOffice())
                .build();
    }
}
