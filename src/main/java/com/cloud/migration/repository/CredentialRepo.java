package com.cloud.migration.repository;

import com.cloud.migration.model.Credential;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CredentialRepo extends CrudRepository<Credential, UUID> {
}
