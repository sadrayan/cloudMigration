package com.cloud.migration.controller;

import com.cloud.migration.exception.EntityNotFoundException;
import com.cloud.migration.model.Volume;
import com.cloud.migration.repository.VolumeRepo;
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
public class VolumeController {

    private VolumeRepo volumeRepo;

    @Autowired
    public VolumeController(VolumeRepo volumeRepo) {
        this.volumeRepo = volumeRepo;
    }

    @GetMapping("/volume")
    public List<Volume> retrieveAllVolume() {
        Iterable<Volume> result = volumeRepo.findAll();
        List<Volume> volumeList = new ArrayList<>();
        result.forEach(volumeList::add);
        return volumeList;
    }

    @GetMapping("/volume/{id}")
    public Volume retrieveVolume(@PathVariable UUID id) {
        Optional<Volume> targetCloud = volumeRepo.findById(id);

        if (!targetCloud.isPresent())
            throw new EntityNotFoundException("id-" + id);

        return targetCloud.get();
    }

    @PostMapping("/volume")
    public ResponseEntity<Object> createVolume(@RequestBody Volume volume) {

        volume.setId(UUID.randomUUID());
        Volume savedVolume = volumeRepo.save(volume);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedVolume.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/volume/{id}")
    public ResponseEntity<Object> updateVolume(@RequestBody Volume volume, @PathVariable UUID id) {
        Optional<Volume> volumeOptional = volumeRepo.findById(id);

        if (!volumeOptional.isPresent())
            return ResponseEntity.notFound().build();

        volume.setId(id);
        volumeRepo.save(volume);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/volume/{id}")
    public ResponseEntity<Object> deleteVolume(@PathVariable UUID id) {
        volumeRepo.deleteById(id);
        return ResponseEntity.ok().build();
    }

}
