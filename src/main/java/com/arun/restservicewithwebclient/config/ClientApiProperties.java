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
