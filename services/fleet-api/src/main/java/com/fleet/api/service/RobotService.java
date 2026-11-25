package com.fleet.api.service;

import com.fleet.api.dto.RobotDto;
import com.fleet.api.grpc.SimulationClient;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class RobotService {
    private final SimulationClient simulation;

    public RobotService(SimulationClient simulation) {
        this.simulation = simulation;
    }

    public List<RobotDto> getRobots() {
        return simulation.getRobots().stream().map(RobotDto::from).toList();
    }
}
