package com.cloud.migration.controller;

import com.cloud.migration.enums.MigrationState;
import com.cloud.migration.exception.MigrationNotFoundException;
import com.cloud.migration.model.Migration;
import com.cloud.migration.repository.MigrationRepo;
import com.cloud.migration.service.MigrationService;
import com.datastax.driver.core.utils.UUIDs;
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
public class MigrationController {

    Logger logger = LoggerFactory.getLogger(MigrationController.class);

    private MigrationRepo migrationRepo;
    private MigrationService migrationService;

    @Autowired
    public MigrationController(MigrationRepo migrationRepo, MigrationService migrationService) {
        this.migrationRepo = migrationRepo;
        this.migrationService = migrationService;
    }

    @GetMapping("/migrate")
    public List<Migration> retrieveAllMigration() {
        Iterable<Migration> result = migrationRepo.findAll();
        List<Migration> migrationList = new ArrayList<>();
        result.forEach(migrationList::add);
        return migrationList;
    }

    @PostMapping("/migrate/{id}/run")
    public ResponseEntity<Object> runMigration(@PathVariable UUID id) {
        Optional<Migration> migration = migrationRepo.findById(id);

        if (!migration.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id-" + id);

        if (!migrationService.validateMountPoint(migration.get()))
            return ResponseEntity.badRequest().body("Requires C:/ mount point (volume) in migration");

        if (migrationService.run(migration.get()))
            return ResponseEntity.ok().build();

        return ResponseEntity.badRequest().body("Something went wrong");

    }

    @GetMapping("/migrate/{id}/status")
    public ResponseEntity<Object> statusMigration(@PathVariable String id) {

        UUID uuid;
        try{
            uuid = UUID.fromString(id);
        } catch (IllegalArgumentException exception){
            //handle the case where string is not valid UUID
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("id-" + id);
        }

        // It should allow to see if migration is finished
        Optional<Migration> migration = migrationRepo.findById(uuid);

        if (!migration.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id-" + id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(migration.get().getMigrationState());
    }

    @GetMapping("/migrate/{id}")
    public Migration retrieveMigration(@PathVariable UUID id) {
        Optional<Migration> migration = migrationRepo.findById(id);

        if (!migration.isPresent())
            throw new MigrationNotFoundException("id-" + id);

        return migration.get();
    }

    @PostMapping("/migrate")
    public ResponseEntity<Object> createMigration(@RequestBody Migration migration) {
        if (migration.getMigrationState() == null)
            migration.setMigrationState(MigrationState.NOT_STARTED);

        migration.setId(UUID.randomUUID());
        Migration savedMigration = migrationRepo.save(migration);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedMigration.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/migrate/{id}")
    public ResponseEntity<Object> updateMigration(@RequestBody Migration migration, @PathVariable UUID id) {
        Optional<Migration> migrationOptional = migrationRepo.findById(id);

        if (!migrationOptional.isPresent())
            return ResponseEntity.notFound().build();

        migration.setId(id);
        migrationRepo.save(migration);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/migrate/{id}")
    public ResponseEntity<Object> deleteMigration(@PathVariable UUID id) {
        migrationRepo.deleteById(id);
        return ResponseEntity.ok().build();
    }

}
