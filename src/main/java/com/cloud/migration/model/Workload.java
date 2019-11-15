package com.cloud.migration.model;

import com.datastax.driver.core.DataType;
import lombok.NonNull;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;


@Table ( "Workload" )
public class Workload implements Serializable {

    private static final long serialVersionUID = 1L;

    @PrimaryKey
    private UUID id;

    @Column(name = "ip", updatable = false, nullable = false)
    private @NonNull String ip;

    //    @CassandraType(type = DataType.Name.UDT, userTypeName = "Credential")

    private @NonNull UUID credential;
    private @NonNull List<UUID> volumes;

    public Workload() {}

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

    public UUID getCredential() {
        return credential;
    }

    public void setCredential(UUID credential) {
        this.credential = credential;
    }

    public List<UUID> getVolumes() {
        return volumes;
    }

    public void setVolumes(List<UUID> volumes) {
        this.volumes = volumes;
    }

    @Override
    public String toString() {
        return "Workload{" +
                "id=" + id +
                ", ip='" + ip + '\'' +
                ", credential=" + credential +
                ", volumes=" + volumes +
                '}';
    }
}
