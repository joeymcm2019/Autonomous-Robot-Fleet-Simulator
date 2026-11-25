package com.fleet.api.repository;

import com.fleet.api.model.FleetTask;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FleetTaskRepository extends JpaRepository<FleetTask, UUID> {}
