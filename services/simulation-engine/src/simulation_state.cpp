#include "simulation_state.hpp"

namespace fleet {

SimulationState initial_state() {
    return {
        .grid = {},
        .robots = {{
            {"AMR-01", {4, 10}, 90, 87, RobotStatus::idle},
            {"AMR-02", {11, 7}, 180, 64, RobotStatus::idle},
            {"AMR-03", {16, 12}, 0, 96, RobotStatus::idle},
        }},
    };
}

}  // namespace fleet
