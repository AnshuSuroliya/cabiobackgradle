package com.poc.cabio.controller;

import com.poc.cabio.request.DistanceRequest;
import com.poc.cabio.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/")
public class LocationController {
    @Autowired
    LocationService locationService;
    @PostMapping("/price")
    public String distanceMatrix(@RequestBody DistanceRequest distanceRequest){
        return locationService.distanceMatrix(distanceRequest.getLocation(),distanceRequest.getDestination());
    }
}
