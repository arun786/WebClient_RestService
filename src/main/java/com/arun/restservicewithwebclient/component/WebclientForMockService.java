package com.arun.restservicewithwebclient.component;

import com.arun.restservicewithwebclient.model.Profile;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * @author arun on 8/1/20
 */
public interface WebclientForMockService {

    String getHealthOfMockService() throws JsonProcessingException;

    ResponseEntity<List<Profile>> getAllProfilesFromMockService();
}
