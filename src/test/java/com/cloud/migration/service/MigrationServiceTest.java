package com.cloud.migration.service;

import com.cloud.migration.model.Credential;
import com.cloud.migration.repository.CredentialRepo;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MigrationServiceTest {

    @Autowired
    private CredentialRepo credentialRepo;

    @Test
    public void testCrudOperations() {
        Credential sample = sampleCredential();
        this.credentialRepo.save(sample);
        Optional<Credential> credentialOptional = this.credentialRepo.findById(sample.getId());
        assertThat ("not found", credentialOptional.isPresent());

        assertThat(credentialOptional.get().getUsername(), equalTo("Sam"));

        this.credentialRepo.delete(credentialOptional.get());
    }
    private Credential sampleCredential() {
        Credential credential = new Credential();
        credential.setId(UUID.randomUUID());
        credential.setUsername("Sam");
        credential.setPassword("HASHED");
        credential.setDomain("");
        return credential;
    }
}