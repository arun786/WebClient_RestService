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
