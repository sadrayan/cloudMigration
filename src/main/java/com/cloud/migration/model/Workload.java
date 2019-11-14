package com.cloud.migration.model;

import lombok.NonNull;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;


@Table ( "Workload" )
public class Workload implements Serializable {

    private static final long serialVersionUID = 1L;

    @PrimaryKey
    private UUID id;
    private @NonNull String ip;
//    private @NonNull Credential credential;
//    private @NonNull List<Volume> volumes;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

//    public Credential getCredential() {
//        return credential;
//    }
//
//    public void setCredential(Credential credential) {
//        this.credential = credential;
//    }
//
//    public List<Volume> getVolumes() {
//        return volumes;
//    }
//
//    public void setVolumes(List<Volume> volumes) {
//        this.volumes = volumes;
//    }
}
