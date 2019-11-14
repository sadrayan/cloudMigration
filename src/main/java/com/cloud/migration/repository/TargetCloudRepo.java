package com.cloud.migration.repository;

import com.cloud.migration.model.TargetCloud;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TargetCloudRepo extends CrudRepository<TargetCloud, UUID> {
}
