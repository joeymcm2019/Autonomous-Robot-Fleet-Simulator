package com.fleet.api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "fleet_tasks")
public class FleetTask {
    @Id
    private UUID id;
    private String description;

    protected FleetTask() {}

    public FleetTask(UUID id, String description) {
        this.id = id;
        this.description = description;
    }

    public UUID getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
}
