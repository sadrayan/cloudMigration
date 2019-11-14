package com.cloud.migration;

import com.cloud.migration.exception.EntityNotFoundException;
import com.cloud.migration.model.Credential;
import com.cloud.migration.repository.CredentialRepo;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
//import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;
import java.util.UUID;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootConfiguration( MigrationApplication.class)
public class MigrationServiceTest {
    @Autowired
    private CredentialRepo credentialRepo;
    @Test
    public void repositoryCrudOperations() {
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