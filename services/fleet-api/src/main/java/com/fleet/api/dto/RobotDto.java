package com.fleet.api.dto;

import com.fleet.simulation.v1.Robot;

public record RobotDto(String id, Position position, int headingDegrees, int batteryPercent, String status) {
    public record Position(int x, int y) {}

    public static RobotDto from(Robot robot) {
        return new RobotDto(robot.getId(), new Position(robot.getPosition().getX(), robot.getPosition().getY()),
                robot.getHeadingDegrees(), robot.getBatteryPercent(), robot.getStatus().name());
    }
}
