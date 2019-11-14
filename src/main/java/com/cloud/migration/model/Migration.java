package com.cloud.migration.model;

import com.cloud.migration.enums.CloudType;
import com.cloud.migration.enums.MigrationState;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;


@Table ( "Migration" )
public class Migration implements Serializable {

    private static final long serialVersionUID = 1L;

    @PrimaryKey
    private UUID id;
    private List<String> mountPoints;
    private Workload workload;

    @Enumerated(EnumType.STRING)
    private MigrationState migrationState;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public List<String> getMountPoints() {
        return mountPoints;
    }

    public void setMountPoints(List<String> mountPoints) {
        this.mountPoints = mountPoints;
    }

    public Workload getWorkload() {
        return workload;
    }

    public void setWorkload(Workload workload) {
        this.workload = workload;
    }

    public MigrationState getMigrationState() {
        return migrationState;
    }

    public void setMigrationState(MigrationState migrationState) {
        this.migrationState = migrationState;
    }
}
