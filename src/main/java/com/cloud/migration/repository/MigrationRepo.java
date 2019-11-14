package com.cloud.migration.repository;

import com.cloud.migration.model.Migration;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MigrationRepo extends CrudRepository<Migration, UUID> {
}
