package com.cloud.migration.controller;

import com.cloud.migration.exception.WorkloadNotFoundException;
import com.cloud.migration.model.Workload;
import com.cloud.migration.repository.WorkloadRepo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
public class WorkloadController {

    Logger logger = LoggerFactory.getLogger(WorkloadController.class);

    private WorkloadRepo workloadRepo;

    @Autowired
    public WorkloadController(WorkloadRepo workloadRepo) {
        this.workloadRepo = workloadRepo;
    }

    @GetMapping("/workload")
    public List<Workload> retrieveAllWorkloads() {
        Iterable<Workload> result = workloadRepo.findAll();
        List<Workload> workloadList = new ArrayList<>();
        result.forEach(workloadList::add);
        return workloadList;
    }

    @GetMapping("/workload/{id}")
    public Workload retrieveWorkload(@PathVariable UUID id) {
        Optional<Workload> workload = workloadRepo.findById(id);

        if (!workload.isPresent())
            throw new WorkloadNotFoundException("id-" + id);

        return workload.get();
    }

    @PostMapping("/workload")
    public ResponseEntity<Object> createWorkload(@RequestBody Workload workload) {

        logger.info(workload.toString());

        if(validateWorkload(workload)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Missing fields");
        }

        workload.setId(UUID.randomUUID());
        Workload savedWorkload = workloadRepo.save(workload);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedWorkload.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    private boolean validateWorkload(Workload workload) {
        return StringUtils.isEmpty(workload.getIp()) || workload.getCredential() == null;
    }

    @PutMapping("/workload/{id}")
    public ResponseEntity<Object> updateWorkload(@RequestBody Workload workload, @PathVariable UUID id) {
        Optional<Workload> workloadOptional = workloadRepo.findById(id);

        if (!workloadOptional.isPresent())
            return ResponseEntity.notFound().build();

        // the IP address of the source should not be modified
        if (!StringUtils.equalsIgnoreCase(workloadOptional.get().getIp(), workload.getIp()) ) {
            return  ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Changing IP is not allowed");
        }

        workload.setId(id);
        workloadRepo.save(workload);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/workload/{id}")
    public ResponseEntity<Object> deleteWorkload(@PathVariable UUID id) {
        workloadRepo.deleteById(id);
        return ResponseEntity.ok().build();
    }

}
