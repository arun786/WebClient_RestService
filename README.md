# WebClient_RestService


## configure webClient 

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

