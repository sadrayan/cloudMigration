package com.cloud.migration.model;

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
    private UUID sourceWorkload;
    private UUID targetCloud;
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

    public UUID getSourceWorkload() {
        return sourceWorkload;
    }

    public void setSourceWorkload(UUID sourceWorkload) {
        this.sourceWorkload = sourceWorkload;
    }

    public UUID getTargetCloud() {
        return targetCloud;
    }

    public void setTargetCloud(UUID targetCloud) {
        this.targetCloud = targetCloud;
    }

    public MigrationState getMigrationState() {
        return migrationState;
    }

    public void setMigrationState(MigrationState migrationState) {
        this.migrationState = migrationState;
    }

    @Override
    public String toString() {
        return "Migration{" +
                "id=" + id +
                ", mountPoints=" + mountPoints +
                ", sourceWorkload=" + sourceWorkload +
                ", targetCloud=" + targetCloud +
                ", migrationState=" + migrationState +
                '}';
    }
}
