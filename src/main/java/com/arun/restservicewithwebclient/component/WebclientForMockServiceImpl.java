package com.arun.restservicewithwebclient.component;

import com.arun.restservicewithwebclient.config.ClientApiProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

/**
 * @author arun on 8/1/20
 */

@Component
public class WebclientForMockServiceImpl implements WebclientForMockService {

    private final WebClient webClient;
    private final ClientApiProperties clientApiProperties;
    private final String ACTUATOR_PATH = "/actuator/health";

    @Autowired
    public WebclientForMockServiceImpl(WebClient webClient, ClientApiProperties clientApiProperties) {
        this.webClient = webClient;
        this.clientApiProperties = clientApiProperties;
    }


    /**
     * Below method checks the Health of the mock service being called.
     *
     * @return - Health with status as up or down
     * @throws JsonProcessingException
     */
    @Override
    public String getHealthOfMockService() throws JsonProcessingException {

        Mono<JsonNode> jsonNodeMono = webClient.get().uri(uriBuilder ->
                uriBuilder
                        .scheme(clientApiProperties.getSchema())
                        .host(clientApiProperties.getHostname())
                        .port(clientApiProperties.getPort())
                        .path(ACTUATOR_PATH)
                        .build())
                .retrieve()
                .bodyToMono(JsonNode.class);
        JsonNode jsonNode = jsonNodeMono.blockOptional().orElseThrow();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> status = objectMapper.readValue(jsonNode.toString(), new TypeReference<>() {
        });

        return status.get("status");
    }

    @Override
    public ResponseEntity<List<Profile>> getAllProfilesFromMockService(int page, int size) {
        return null;
    }
}
