package com.cloud.migration.service;

import com.cloud.migration.controller.CredentialController;
import com.cloud.migration.model.Credential;
import com.cloud.migration.model.Volume;
import com.cloud.migration.model.Workload;
import com.cloud.migration.repository.CredentialRepo;
import com.cloud.migration.repository.VolumeRepo;
import com.cloud.migration.repository.WorkloadRepo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DaoTest {

    private Logger logger = LoggerFactory.getLogger(CredentialController.class);


    @Autowired
    private CredentialRepo credentialRepo;

    @Autowired
    private VolumeRepo volumeRepo;

    @Autowired
    private WorkloadRepo workloadRepo;

    @Test
    public void testCrudOperations() {
        Credential sample = sampleCredential();
        this.credentialRepo.save(sample);
        Optional<Credential> credentialOptional = this.credentialRepo.findById(sample.getId());
        assertThat ("not found", credentialOptional.isPresent());

        assertThat(credentialOptional.get().getUsername(), equalTo("Sam"));
        logger.info(credentialOptional.get().toString());

        credentialOptional.get().setDomain("new domain");
        this.credentialRepo.save(credentialOptional.get());

        credentialOptional = this.credentialRepo.findById(sample.getId());
        assertThat(credentialOptional.get().getUsername(), equalTo("Sam"));
        logger.info(credentialOptional.get().toString());


        this.credentialRepo.delete(credentialOptional.get());
    }

    @Test
    public void testVolumeCrudOperations() {
        Volume sample = new Volume(UUID.randomUUID(), "c:/", 123232);
        this.volumeRepo.save(sample);
        Optional<Volume> volumeOptional = this.volumeRepo.findById(sample.getId());
        assertThat ("not found", volumeOptional.isPresent());

        assertThat(volumeOptional.get().getMountPoint(), equalTo("c:/"));

        this.volumeRepo.delete(volumeOptional.get());
    }

    @Test
    public void testWorkloadCrudOperations() {
        Credential credential = sampleCredential();
        this.credentialRepo.save(credential);

        Workload sample = new Workload();
        sample.setId(UUID.randomUUID());
        sample.setIp("127.0.0.1");
        sample.setCredential(credential.getId());
        sample.setVolumes(Arrays.asList(UUID.randomUUID(), UUID.randomUUID()));

        this.workloadRepo.save(sample);
        Optional<Workload> optionalWorkload = this.workloadRepo.findById(sample.getId());
        assertThat ("not found", optionalWorkload.isPresent());

        logger.info(optionalWorkload.get().toString());

        assertThat(optionalWorkload.get().getIp(), equalTo("127.0.0.1"));

        this.workloadRepo.delete(optionalWorkload.get());
    }

    private Credential sampleCredential() {
        Credential credential = new Credential();
        credential.setId(UUID.randomUUID());
        credential.setUsername("Sam");
        credential.setPassword("HASHED");
        credential.setDomain("domain.com");
        return credential;
    }
}