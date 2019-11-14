package com.cloud.migration.controller;

import com.cloud.migration.exception.TargetCloudNotFoundException;
import com.cloud.migration.model.TargetCloud;
import com.cloud.migration.repository.TargetCloudRepo;
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
public class TargetCloudController {

    private TargetCloudRepo targetCloudRepo;

    @Autowired
    public TargetCloudController(TargetCloudRepo targetCloudRepo) {
        this.targetCloudRepo = targetCloudRepo;
    }

    @GetMapping("/target")
    public List<TargetCloud> retrieveAllTargetCloud() {
        Iterable<TargetCloud> result = targetCloudRepo.findAll();
        List<TargetCloud> targetCloudList = new ArrayList<>();
        result.forEach(targetCloudList::add);
        return targetCloudList;
    }

    @GetMapping("/target/{id}")
    public TargetCloud retrieveTargetCloud(@PathVariable UUID id) {
        Optional<TargetCloud> targetCloud = targetCloudRepo.findById(id);

        if (!targetCloud.isPresent())
            throw new TargetCloudNotFoundException("id-" + id);

        return targetCloud.get();
    }

    @PostMapping("/target")
    public ResponseEntity<Object> createTargetCloud(@RequestBody TargetCloud targetCloud) {
        TargetCloud savedTargetCloud = targetCloudRepo.save(targetCloud);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedTargetCloud.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/target/{id}")
    public ResponseEntity<Object> updateTargetCloud(@RequestBody TargetCloud targetCloud, @PathVariable UUID id) {
        Optional<TargetCloud> targetCloudOptional = targetCloudRepo.findById(id);

        if (!targetCloudOptional.isPresent())
            return ResponseEntity.notFound().build();

        targetCloud.setId(id);
        targetCloudRepo.save(targetCloud);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/target/{id}")
    public ResponseEntity<Object> deleteTargetCloud(@PathVariable UUID id) {
        targetCloudRepo.deleteById(id);
        return ResponseEntity.ok().build();
    }

}
