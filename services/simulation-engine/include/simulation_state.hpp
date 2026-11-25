#pragma once

#include <array>
#include <string_view>

namespace fleet {

struct Position {
    int x;
    int y;
    constexpr bool operator==(const Position&) const = default;
};

enum class RobotStatus { idle, moving, charging, error };

struct Robot {
    std::string_view id;
    Position position;
    int heading_degrees;
    int battery_percent;
    RobotStatus status;
};

struct WarehouseGrid {
    int width = 20;
    int height = 16;

    [[nodiscard]] constexpr bool contains(Position position) const noexcept {
        return position.x >= 0 && position.x < width &&
               position.y >= 0 && position.y < height;
    }
};

struct SimulationState {
    WarehouseGrid grid;
    std::array<Robot, 3> robots;
    int tick = 0;
    bool running = false;
};

[[nodiscard]] SimulationState initial_state();

}  // namespace fleet
