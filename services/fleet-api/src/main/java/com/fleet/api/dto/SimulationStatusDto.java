package com.fleet.api.dto;

import com.fleet.simulation.v1.SimulationStatus;

public record SimulationStatusDto(long tick, boolean running, int gridWidth, int gridHeight) {
    public static SimulationStatusDto from(SimulationStatus status) {
        return new SimulationStatusDto(status.getTick(), status.getRunning(), status.getGridWidth(), status.getGridHeight());
    }
}
