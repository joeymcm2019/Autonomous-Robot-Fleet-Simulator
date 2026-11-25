package com.fleet.api;

import com.fleet.api.dto.RobotDto;
import com.fleet.api.grpc.SimulationClient;
import com.fleet.api.service.RobotService;
import com.fleet.simulation.v1.Position;
import com.fleet.simulation.v1.Robot;
import com.fleet.simulation.v1.RobotStatus;
import java.util.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RobotServiceTest {
    @Test
    void mapsEngineStateToRestRecords() {
        var client = mock(SimulationClient.class);
        when(client.getRobots()).thenReturn(List.of(Robot.newBuilder().setId("TEST-1")
                .setPosition(Position.newBuilder().setX(4).setY(7))
                .setHeadingDegrees(180).setBatteryPercent(77).setStatus(RobotStatus.CHARGING).build()));
        assertEquals(List.of(new RobotDto("TEST-1", new RobotDto.Position(4, 7), 180, 77, "CHARGING")),
                new RobotService(client).getRobots());
    }
}
