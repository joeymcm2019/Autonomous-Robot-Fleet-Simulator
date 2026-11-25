#include "simulation_state.hpp"

#include <gtest/gtest.h>
#include <set>

TEST(WarehouseGrid, ValidatesBothAxes) {
    const fleet::WarehouseGrid grid;
    EXPECT_TRUE(grid.contains({0, 0}));
    EXPECT_TRUE(grid.contains({grid.width - 1, grid.height - 1}));
    EXPECT_FALSE(grid.contains({-1, 0}));
    EXPECT_FALSE(grid.contains({0, -1}));
    EXPECT_FALSE(grid.contains({grid.width, 0}));
    EXPECT_FALSE(grid.contains({0, grid.height}));
}

TEST(SimulationState, StartsStationaryAtTickZero) {
    const auto state = fleet::initial_state();
    EXPECT_FALSE(state.running);
    EXPECT_EQ(state.tick, 0);
    EXPECT_EQ(state.grid.width, 20);
    EXPECT_EQ(state.grid.height, 16);
    EXPECT_EQ(state.robots.size(), 3);
}

TEST(Robots, StartInDistinctValidCellsWithUniqueIds) {
    const auto state = fleet::initial_state();
    std::set<std::string_view> ids;
    std::set<std::pair<int, int>> positions;
    for (const auto& robot : state.robots) {
        EXPECT_TRUE(ids.insert(robot.id).second);
        EXPECT_TRUE(state.grid.contains(robot.position));
        EXPECT_TRUE(positions.emplace(robot.position.x, robot.position.y).second);
        EXPECT_EQ(robot.status, fleet::RobotStatus::idle);
        EXPECT_GE(robot.battery_percent, 0);
        EXPECT_LE(robot.battery_percent, 100);
        EXPECT_GE(robot.heading_degrees, 0);
        EXPECT_LT(robot.heading_degrees, 360);
    }
}
