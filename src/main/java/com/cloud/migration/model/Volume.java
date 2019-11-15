package com.cloud.migration.model;

import lombok.NonNull;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.data.cassandra.core.mapping.UserDefinedType;

import java.io.Serializable;
import java.util.UUID;

@UserDefinedType( "Volume" )
public class Volume implements Serializable {

    private static final long serialVersionUID = 1L;

    @PrimaryKey
    private UUID id;
    private @NonNull String mountPoint;
    private @NonNull int size;

    public Volume(UUID id, @NonNull String mountPoint, @NonNull int size) {
        this.id = id;
        this.mountPoint = mountPoint;
        this.size = size;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getMountPoint() {
        return mountPoint;
    }

    public void setMountPoint(String mountPoint) {
        this.mountPoint = mountPoint;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
