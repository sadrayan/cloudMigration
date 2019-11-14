package com.cloud.migration.controller;

import com.cloud.migration.exception.MigrationNotFoundException;
import com.cloud.migration.model.Migration;
import com.cloud.migration.repository.MigrationRepo;
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
public class MigrationController {

    private MigrationRepo migrationRepo;

    @Autowired
    public MigrationController(MigrationRepo migrationRepo) {
        this.migrationRepo = migrationRepo;
    }

    @GetMapping("/migrate")
    public List<Migration> retrieveAllMigration() {
        Iterable<Migration> result = migrationRepo.findAll();
        List<Migration> migrationList = new ArrayList<>();
        result.forEach(migrationList::add);
        return migrationList;
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
