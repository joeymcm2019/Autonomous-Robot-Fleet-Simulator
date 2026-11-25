package com.fleet.api.config;

import com.fleet.api.websocket.SimulationSocket;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration(proxyBeanMethods = false)
@EnableWebSocket
public class WebConfiguration implements WebMvcConfigurer, WebSocketConfigurer {
    private final String[] allowedOrigins;

    public WebConfiguration(@Value("${fleet.cors-origins}") String origins) {
        allowedOrigins = origins.strip().split("\\s*,\\s*");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**").allowedOrigins(allowedOrigins).allowedMethods("GET");
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new SimulationSocket(), "/ws/simulation").setAllowedOrigins(allowedOrigins);
    }
}
