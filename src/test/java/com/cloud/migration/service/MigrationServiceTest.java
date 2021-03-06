package com.cloud.migration.service;

import com.cloud.migration.enums.MigrationState;
import com.cloud.migration.exception.WorkloadNotFoundException;
import com.cloud.migration.model.Migration;
import com.cloud.migration.model.TargetCloud;
import com.cloud.migration.model.Workload;
import com.cloud.migration.repository.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@SpringBootTest
public class MigrationServiceTest {

    private Logger logger = LoggerFactory.getLogger(MigrationServiceTest.class);

    @InjectMocks private MigrationService migrationService;
    @Mock private CredentialRepo credentialRepo;
    @Mock private VolumeRepo volumeRepo;
    @Mock private WorkloadRepo workloadRepo;
    @Mock private TargetCloudRepo targetCloudRepo;
    @Mock private MigrationRepo migrationRepo;


    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }



    // TODO: complete, expand
    @Test()
    public void testRunMigration() {
        when(workloadRepo.findById(any()))
                .thenReturn(Optional.of(new Workload()))
                .thenReturn(Optional.of(new Workload()));
        when(targetCloudRepo.findById(any()))
                .thenReturn(Optional.of(new TargetCloud()));
        Migration migration = new Migration();
        migration.setMigrationState(MigrationState.NOT_STARTED);
        migrationService.run(migration);
    }


    @Test(expected = WorkloadNotFoundException.class)
    public void testRunMigration_workload_not_found() {
        when(workloadRepo.findById(any())).thenReturn(Optional.empty());
        Migration migration = new Migration();
        migrationService.run(migration);
    }

}