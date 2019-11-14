package com.cloud.migration.model;

import com.cloud.migration.enums.CloudType;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;
import java.util.UUID;

public class TargetCloud implements Serializable {

    private static final long serialVersionUID = 1L;

    @PrimaryKey
    private UUID id;
    @Enumerated(EnumType.STRING)
    private CloudType cloudType;

//    private Credential credential;
//    private Workload workload;

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

//    public Credential getCredential() {
//        return credential;
//    }
//
//    public void setCredential(Credential credential) {
//        this.credential = credential;
//    }
//
//    public Workload getWorkload() {
//        return workload;
//    }
//
//    public void setWorkload(Workload workload) {
//        this.workload = workload;
//    }
}
