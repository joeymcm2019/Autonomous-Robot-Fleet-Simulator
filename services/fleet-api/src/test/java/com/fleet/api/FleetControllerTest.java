package com.fleet.api;

import com.fleet.api.controller.FleetController;
import com.fleet.api.grpc.SimulationClient;
import com.fleet.api.service.RobotService;
import io.grpc.Status;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FleetControllerTest {
    private final SimulationClient client = mock(SimulationClient.class);
    private final RobotService robots = mock(RobotService.class);
    private final org.springframework.test.web.servlet.MockMvc mvc =
            MockMvcBuilders.standaloneSetup(new FleetController(client, robots)).build();

    @Test
    void reportsApiAndEngineHealthIndependently() throws Exception {
        when(client.isReachable()).thenReturn(true, false);
        mvc.perform(get("/api/health")).andExpect(status().isOk())
                .andExpect(jsonPath("$.apiOperational").value(true))
                .andExpect(jsonPath("$.simulationReachable").value(true));
        mvc.perform(get("/api/health")).andExpect(status().isOk())
                .andExpect(jsonPath("$.simulationReachable").value(false));
    }

    @Test
    void returnsServiceUnavailableWhenEngineRequestFails() throws Exception {
        when(robots.getRobots()).thenThrow(Status.UNAVAILABLE.asRuntimeException());
        mvc.perform(get("/api/robots")).andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.detail").value("Simulation engine request failed."));
    }
}
