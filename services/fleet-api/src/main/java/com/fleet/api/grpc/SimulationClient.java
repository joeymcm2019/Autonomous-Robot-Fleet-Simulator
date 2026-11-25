package com.fleet.api.grpc;

import com.fleet.simulation.v1.GetRobotsRequest;
import com.fleet.simulation.v1.GetSimulationStatusRequest;
import com.fleet.simulation.v1.HealthRequest;
import com.fleet.simulation.v1.Robot;
import com.fleet.simulation.v1.SimulationServiceGrpc;
import com.fleet.simulation.v1.SimulationStatus;
import io.grpc.Channel;
import io.grpc.StatusRuntimeException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SimulationClient {
    private final SimulationServiceGrpc.SimulationServiceBlockingStub stub;
    private final long deadlineMillis;

    public SimulationClient(Channel channel, @Value("${fleet.simulation.deadline-ms}") long deadlineMillis) {
        this.stub = SimulationServiceGrpc.newBlockingStub(channel);
        this.deadlineMillis = deadlineMillis;
    }

    public boolean isReachable() {
        try {
            return withDeadline().checkHealth(HealthRequest.getDefaultInstance()).getHealthy();
        } catch (StatusRuntimeException exception) {
            return false;
        }
    }

    public List<Robot> getRobots() {
        return withDeadline().getRobots(GetRobotsRequest.getDefaultInstance()).getRobotsList();
    }

    public SimulationStatus getStatus() {
        return withDeadline().getSimulationStatus(GetSimulationStatusRequest.getDefaultInstance()).getStatus();
    }

    private SimulationServiceGrpc.SimulationServiceBlockingStub withDeadline() {
        return stub.withDeadlineAfter(deadlineMillis, TimeUnit.MILLISECONDS);
    }
}
