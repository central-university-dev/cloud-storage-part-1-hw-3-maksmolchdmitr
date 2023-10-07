package maks.molch.dmitr.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "server")
public record ServerConfig(
        int port
) {
}
