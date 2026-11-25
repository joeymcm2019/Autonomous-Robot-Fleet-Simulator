package com.fleet.api;

import com.fleet.api.grpc.SimulationClient;
import com.fleet.simulation.v1.*;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SimulationClientTest {
    @Test
    void callsAllRpcMethodsAndHandlesEngineDisconnection() throws Exception {
        var name = InProcessServerBuilder.generateName();
        var server = InProcessServerBuilder.forName(name).directExecutor()
                .addService(new TestSimulation()).build().start();
        var channel = InProcessChannelBuilder.forName(name).directExecutor().build();
        try {
            var client = new SimulationClient(channel, 1000);
            assertTrue(client.isReachable());
            assertEquals("TEST-1", client.getRobots().getFirst().getId());
            assertEquals(20, client.getStatus().getGridWidth());
            server.shutdownNow();
            assertFalse(client.isReachable());
        } finally {
            channel.shutdownNow();
            server.shutdownNow();
        }
    }

    private static class TestSimulation extends SimulationServiceGrpc.SimulationServiceImplBase {
        @Override
        public void checkHealth(HealthRequest request, StreamObserver<HealthResponse> observer) {
            observer.onNext(HealthResponse.newBuilder().setHealthy(true).build());
            observer.onCompleted();
        }

        @Override
        public void getRobots(GetRobotsRequest request, StreamObserver<GetRobotsResponse> observer) {
            observer.onNext(GetRobotsResponse.newBuilder().addRobots(Robot.newBuilder().setId("TEST-1")).build());
            observer.onCompleted();
        }

        @Override
        public void getSimulationStatus(GetSimulationStatusRequest request,
                                        StreamObserver<GetSimulationStatusResponse> observer) {
            observer.onNext(GetSimulationStatusResponse.newBuilder()
                    .setStatus(SimulationStatus.newBuilder().setGridWidth(20)).build());
            observer.onCompleted();
        }
    }
}
