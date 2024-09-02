package com.poc.cabio.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class LocationServiceImpl implements LocationService{
    @Override
    public String distanceMatrix(String location, String destination) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity("https://maps.googleapis.com/maps/api/distancematrix/json\n" +
                    "  ?destinations=" + destination +
                    "  &origins=" + location +
                    "  &units=imperial\n" +
                    "  &key=AIzaSyCYkkDA-nwSfQwK8HYAgg-9vE-c9HolewU", String.class);
            return response.toString();
        }catch (NullPointerException e){
            return "Location or destination value is null";
        }catch (Exception e){
            return "MapsAPI not working";
        }
    }
}
