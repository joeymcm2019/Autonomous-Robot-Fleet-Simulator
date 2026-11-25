package com.fleet.api.controller;

import com.fleet.api.dto.RobotDto;
import com.fleet.api.dto.SimulationStatusDto;
import com.fleet.api.grpc.SimulationClient;
import com.fleet.api.service.RobotService;
import io.grpc.StatusRuntimeException;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class FleetController {
    private final SimulationClient simulation;
    private final RobotService robots;

    public FleetController(SimulationClient simulation, RobotService robots) {
        this.simulation = simulation;
        this.robots = robots;
    }

    public record Health(boolean apiOperational, boolean simulationReachable) {}

    @GetMapping("/health")
    public Health health() {
        return new Health(true, simulation.isReachable());
    }

    @GetMapping("/robots")
    public List<RobotDto> robots() {
        return robots.getRobots();
    }

    @GetMapping("/simulation/status")
    public SimulationStatusDto status() {
        return SimulationStatusDto.from(simulation.getStatus());
    }

    @ExceptionHandler(StatusRuntimeException.class)
    ProblemDetail unavailable(StatusRuntimeException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.SERVICE_UNAVAILABLE, "Simulation engine request failed.");
    }
}
