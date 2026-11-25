package com.fleet.api.websocket;

import org.springframework.web.socket.handler.TextWebSocketHandler;

/** Phase 1 accepts connections. State is read through REST. */
public class SimulationSocket extends TextWebSocketHandler {}
