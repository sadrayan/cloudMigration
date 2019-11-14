package com.cloud.migration.repository;

import com.cloud.migration.model.Volume;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VolumeRepo extends CrudRepository<Volume, UUID> {
}
