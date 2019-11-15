package com.cloud.migration.model;

import com.cloud.migration.enums.CloudType;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Table ( "TargetCloud" )
public class TargetCloud implements Serializable {

    private static final long serialVersionUID = 1L;

    @PrimaryKey
    private UUID id;
    @Enumerated(EnumType.STRING)
    private CloudType cloudType;

    private List<UUID> credentials;
    private UUID targetWorkload;

    public TargetCloud(UUID id, CloudType cloudType, List<UUID> credentials, UUID targetWorkload) {
        this.id = id;
        this.cloudType = cloudType;
        this.credentials = credentials;
        this.targetWorkload = targetWorkload;
    }

    public TargetCloud() {}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public CloudType getCloudType() {
        return cloudType;
    }

    public void setCloudType(CloudType cloudType) {
        this.cloudType = cloudType;
    }

    public List<UUID> getCredentials() {
        return credentials;
    }

    public void setCredentials(List<UUID> credentials) {
        this.credentials = credentials;
    }

    public UUID getTargetWorkload() {
        return targetWorkload;
    }

    public void setTargetWorkload(UUID targetWorkload) {
        this.targetWorkload = targetWorkload;
    }

    @Override
    public String toString() {
        return "TargetCloud{" +
                "id=" + id +
                ", cloudType=" + cloudType +
                ", credentials=" + credentials +
                ", targetWorkload=" + targetWorkload +
                '}';
    }
}
