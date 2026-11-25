package com.fleet.api.config;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class GrpcConfiguration {
    @Bean(destroyMethod = "shutdownNow")
    ManagedChannel simulationChannel(
            @Value("${fleet.simulation.host}") String host,
            @Value("${fleet.simulation.port}") int port) {
        return ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
    }
}
