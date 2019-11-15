package com.cloud.migration.service;


import com.cloud.migration.controller.MigrationController;
import com.cloud.migration.enums.MigrationState;
import com.cloud.migration.exception.EntityNotFoundException;
import com.cloud.migration.exception.MigrationNotFoundException;
import com.cloud.migration.exception.WorkloadNotFoundException;
import com.cloud.migration.model.Migration;
import com.cloud.migration.model.TargetCloud;
import com.cloud.migration.model.Volume;
import com.cloud.migration.model.Workload;
import com.cloud.migration.repository.MigrationRepo;
import com.cloud.migration.repository.TargetCloudRepo;
import com.cloud.migration.repository.VolumeRepo;
import com.cloud.migration.repository.WorkloadRepo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class MigrationService {

    Logger logger = LoggerFactory.getLogger(MigrationService.class);


    @Autowired
    private MigrationRepo migrationRepo;
    @Autowired
    private WorkloadRepo workloadRepo;
    @Autowired
    private TargetCloudRepo targetCloudRepo;
    @Autowired
    private VolumeRepo volumeRepo;


    public boolean run (Migration migration) {

        // copy source object to the target field of TargetCloud
        Optional<Workload> workloadOptional = workloadRepo.findById(migration.getSourceWorkload());

        if (!workloadOptional.isPresent())
            throw new WorkloadNotFoundException("id-" + migration.getSourceWorkload());

        Workload sourceWorkload = workloadOptional.get();

        // Target should only have volumes that are selected in the migration.
        // For example, if source has: C:\ D: and E:\ and only C: was selected, target should only have C:\
        Optional<TargetCloud> targetCloudOptional = targetCloudRepo.findById(migration.getTargetCloud());

        if (!targetCloudOptional.isPresent())
            throw new EntityNotFoundException("id-" + migration.getTargetCloud());

        // copy source object to the target field of TargetCloud
        Optional<Workload> targetWorkloadOptional = workloadRepo.findById(targetCloudOptional.get().getTargetWorkload());

        Workload targetWorkload;
        //TODO: assumption, create if not exist
        if (!targetWorkloadOptional.isPresent()) {
            logger.info("missing target workload, creating new");
            targetWorkload = new Workload();
            targetWorkload.setId(UUID.randomUUID());
        } else {
            targetWorkload = targetWorkloadOptional.get();
        }


        Iterable<Volume> volumeIterable = volumeRepo.findAllById(sourceWorkload.getVolumes());
        for (Volume volume : volumeIterable) {
            if (migration.getMountPoints().contains(volume.getMountPoint()))
                targetWorkload.getVolumes().add(volume.getId());
        }


        workloadRepo.save(targetWorkload);

        targetCloudOptional.get().setTargetWorkload(targetWorkload.getId());

        targetCloudRepo.save(targetCloudOptional.get());

        // Update migration by setting the status
        migration.setMigrationState(MigrationState.RUNNING);
        migrationRepo.save(migration);

//        sleep();

        // Finalize migration by setting the status
        migration.setMigrationState(MigrationState.SUCCESS);
        migrationRepo.save(migration);

        return true;
    }

    /**
     * Simple function to simulate unit of work time.
     * TODO: handle errors
     */
    private void sleep () {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Logic to not allow running migrations when volume C:\ is not allowed
     * Assumption: if migration mount points don't have C:\, abort.
     * Other assumption would be if volume available in source workload has C:\ (slight implementation change)
     * @param migration
     * @return
     */
    public boolean validateMountPoint(Migration migration) {
        for (String moutPoint: migration.getMountPoints()) {
            if (StringUtils.equalsIgnoreCase(moutPoint, "C:/"))
                return true;
        }
        return false;
    }
}
