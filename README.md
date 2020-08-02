# WebClient_RestService


## Configure webClient 

    package com.arun.restservicewithwebclient.client;
    
    import io.netty.channel.ChannelOption;
    import io.netty.handler.timeout.ReadTimeoutHandler;
    import io.netty.handler.timeout.WriteTimeoutHandler;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.http.client.reactive.ReactorClientHttpConnector;
    import org.springframework.web.reactive.function.client.WebClient;
    import reactor.netty.http.client.HttpClient;
    import reactor.netty.tcp.TcpClient;
    
    /**
     * Webclient configuration
     *
     * @author arun on 8/1/20
     */
    
    @Configuration
    public class SimpleWebClientConfiguration {
    
        /**
         * The below bean is created with a configuration of
         * <p>
         * 1. connection timeout of 2000 milliseconds
         * 2. read time out of 2 s
         * 3. write time our of 2 s
         *
         * @return a webclient
         */
        @Bean
        public WebClient webClient() {
            TcpClient tcpClient = TcpClient.create()
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 2_000)
                    .doOnConnected(connection -> connection.addHandlerLast(new ReadTimeoutHandler(2))
                            .addHandlerLast(new WriteTimeoutHandler(2)));
    
            return WebClient.builder().clientConnector(new ReactorClientHttpConnector(HttpClient.from(tcpClient))).build();
        }
    }

## Configure the details of the mockService which will be called using MockService

### Application.yml

    client:
      url:
        schema: http
        hostname: localhost
        port: 8443
        
### ClientApiProperties


    package com.arun.restservicewithwebclient.config;
    
    import lombok.Getter;
    import lombok.Setter;
    import org.springframework.boot.context.properties.ConfigurationProperties;
    import org.springframework.boot.context.properties.EnableConfigurationProperties;
    import org.springframework.context.annotation.Configuration;
    
    /**
     * @author arun on 8/1/20
     */
    
    @Configuration
    @EnableConfigurationProperties
    @ConfigurationProperties(prefix = "client.url")
    @Getter
    @Setter
    public class ClientApiProperties {
        private String schema;
        private String hostname;
        private String port;
    }


### check the status of Mock Service, calling mock service with webclient

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


A groovy configuration for Spock 

    package com.arun.restservicewithwebclient.groovy
    
    import com.arun.restservicewithwebclient.component.WebclientForMockService
    import org.springframework.beans.factory.annotation.Autowired
    import org.springframework.boot.test.context.SpringBootTest
    import spock.lang.Specification
    import spock.lang.Stepwise
    
    /**
     * @author arun on 8/1/20
     */
    
    @SpringBootTest
    @Stepwise
    class ITSpock extends Specification {
    
        @Autowired
        private WebclientForMockService webclientForMockService;
    
        def "Assert Bean Creation for Component"() {
            expect: "Bean created successfully"
            webclientForMockService != null
        }
    
        def "Check the status of MockService"() {
            expect: "UP"
            def status = webclientForMockService.healthOfMockService
            when: "Check for Health"
            then:
            status == "UP"
        }
    }

