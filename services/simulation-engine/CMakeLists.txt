cmake_minimum_required(VERSION 3.22)
project(fleet_simulation LANGUAGES CXX)
set(CMAKE_CXX_STANDARD 20)
set(CMAKE_CXX_STANDARD_REQUIRED ON)
set(CMAKE_CXX_EXTENSIONS OFF)

add_library(simulation_state src/simulation_state.cpp)
target_include_directories(simulation_state PUBLIC include)

find_package(Protobuf CONFIG QUIET)
if(NOT Protobuf_FOUND)
    find_package(Protobuf REQUIRED)
endif()
find_package(gRPC CONFIG REQUIRED)

set(PROTO_DIR "${CMAKE_CURRENT_SOURCE_DIR}/../../proto")
set(GENERATED_DIR "${CMAKE_CURRENT_BINARY_DIR}/generated")
file(MAKE_DIRECTORY "${GENERATED_DIR}")
add_custom_command(
    OUTPUT "${GENERATED_DIR}/simulation.pb.cc" "${GENERATED_DIR}/simulation.pb.h"
           "${GENERATED_DIR}/simulation.grpc.pb.cc" "${GENERATED_DIR}/simulation.grpc.pb.h"
    COMMAND protobuf::protoc "--proto_path=${PROTO_DIR}"
            "--cpp_out=${GENERATED_DIR}" "--grpc_out=${GENERATED_DIR}"
            "--plugin=protoc-gen-grpc=$<TARGET_FILE:gRPC::grpc_cpp_plugin>"
            "${PROTO_DIR}/simulation.proto"
    DEPENDS "${PROTO_DIR}/simulation.proto" protobuf::protoc gRPC::grpc_cpp_plugin
    VERBATIM
)

add_library(simulation_rpc "${GENERATED_DIR}/simulation.pb.cc" "${GENERATED_DIR}/simulation.grpc.pb.cc")
target_include_directories(simulation_rpc PUBLIC "${GENERATED_DIR}")
target_link_libraries(simulation_rpc PUBLIC protobuf::libprotobuf gRPC::grpc++)
add_executable(simulation-engine src/main.cpp)
target_link_libraries(simulation-engine PRIVATE simulation_state simulation_rpc)

include(CTest)
if(BUILD_TESTING)
    find_package(GTest CONFIG REQUIRED)
    add_executable(simulation_tests tests/simulation_test.cpp)
    target_link_libraries(simulation_tests PRIVATE GTest::gtest_main simulation_state)
    include(GoogleTest)
    gtest_discover_tests(simulation_tests)
endif()
