package com.cloud.migration.controller;

import com.cloud.migration.exception.WorkloadNotFoundException;
import com.cloud.migration.model.Workload;
import com.cloud.migration.repository.WorkloadRepo;
import org.springframework.beans.factory.annotation.Autowired;
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
        Workload savedWorkload = workloadRepo.save(workload);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedWorkload.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/workload/{id}")
    public ResponseEntity<Object> updateWorkload(@RequestBody Workload workload, @PathVariable UUID id) {
        Optional<Workload> WorkloadOptional = workloadRepo.findById(id);

        if (!WorkloadOptional.isPresent())
            return ResponseEntity.notFound().build();

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
