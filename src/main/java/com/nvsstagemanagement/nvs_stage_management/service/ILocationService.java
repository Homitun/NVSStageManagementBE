package com.nvsstagemanagement.nvs_stage_management.service;

import com.nvsstagemanagement.nvs_stage_management.dto.location.CreateLocationDTO;
import com.nvsstagemanagement.nvs_stage_management.dto.location.LocationDTO;
import com.nvsstagemanagement.nvs_stage_management.dto.location.UpdateLocationDTO;

import java.util.List;

public interface ILocationService {
    List<LocationDTO> getAllLocations();
    LocationDTO getLocationById(int locationID);
    LocationDTO createLocation(CreateLocationDTO createLocationDTO);
    LocationDTO updateLocation(int locationID, UpdateLocationDTO updateLocationDTO);
    void deleteLocation(int locationID);
}
