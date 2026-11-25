#include "simulation_state.hpp"
#include "simulation.grpc.pb.h"

#include <chrono>
#include <cstdlib>
#include <iostream>
#include <string>
#include <grpcpp/grpcpp.h>

namespace {
namespace rpc = fleet::simulation::v1;

rpc::RobotStatus to_wire(fleet::RobotStatus status) {
    switch (status) {
        case fleet::RobotStatus::idle: return rpc::IDLE;
        case fleet::RobotStatus::moving: return rpc::MOVING;
        case fleet::RobotStatus::charging: return rpc::CHARGING;
        case fleet::RobotStatus::error: return rpc::ERROR;
    }
    return rpc::ROBOT_STATUS_UNSPECIFIED;
}

class SimulationService final : public rpc::SimulationService::Service {
public:
    grpc::Status CheckHealth(grpc::ServerContext*, const rpc::HealthRequest*,
                             rpc::HealthResponse* response) override {
        response->set_healthy(true);
        return grpc::Status::OK;
    }

    grpc::Status GetRobots(grpc::ServerContext*, const rpc::GetRobotsRequest*,
                           rpc::GetRobotsResponse* response) override {
        for (const auto& robot : state_.robots) {
            auto& output = *response->add_robots();
            output.set_id(std::string(robot.id));
            output.mutable_position()->set_x(robot.position.x);
            output.mutable_position()->set_y(robot.position.y);
            output.set_heading_degrees(robot.heading_degrees);
            output.set_battery_percent(robot.battery_percent);
            output.set_status(to_wire(robot.status));
        }
        return grpc::Status::OK;
    }

    grpc::Status GetSimulationStatus(grpc::ServerContext*,
                                     const rpc::GetSimulationStatusRequest*,
                                     rpc::GetSimulationStatusResponse* response) override {
        auto& output = *response->mutable_status();
        output.set_running(state_.running);
        output.set_tick(state_.tick);
        output.set_grid_width(state_.grid.width);
        output.set_grid_height(state_.grid.height);
        return grpc::Status::OK;
    }

private:
    const fleet::SimulationState state_ = fleet::initial_state();
};

int check_health(const std::string& port) {
    auto channel = grpc::CreateChannel("127.0.0.1:" + port,
                                       grpc::InsecureChannelCredentials());
    auto stub = rpc::SimulationService::NewStub(channel);
    grpc::ClientContext context;
    context.set_deadline(std::chrono::system_clock::now() + std::chrono::seconds(2));
    rpc::HealthResponse response;
    const auto status = stub->CheckHealth(&context, rpc::HealthRequest{}, &response);
    return status.ok() && response.healthy() ? EXIT_SUCCESS : EXIT_FAILURE;
}
}  // namespace

int main(int argc, char** argv) {
    const auto* configured_port = std::getenv("SIMULATION_PORT");
    const std::string port = configured_port ? configured_port : "50051";
    if (argc == 2 && std::string_view(argv[1]) == "--health-check") {
        return check_health(port);
    }

    const auto* configured_host = std::getenv("SIMULATION_BIND_HOST");
    const std::string address = std::string(configured_host ? configured_host : "127.0.0.1") + ":" + port;
    SimulationService service;
    grpc::ServerBuilder builder;
    builder.AddListeningPort(address, grpc::InsecureServerCredentials());
    builder.RegisterService(&service);
    const auto server = builder.BuildAndStart();
    if (!server) {
        std::cerr << "Could not bind gRPC server to " << address << '\n';
        return EXIT_FAILURE;
    }
    std::cout << "Simulation gRPC server listening on " << address << std::endl;
    server->Wait();
}
