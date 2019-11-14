package com.cloud.migration.controller;

import com.cloud.migration.exception.EntityNotFoundException;
import com.cloud.migration.model.Credential;
import com.cloud.migration.repository.CredentialRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@RestController
public class CredentialController {

    Logger logger = LoggerFactory.getLogger(CredentialController.class);

    private CredentialRepo credentialRepo;
    private static final String SALT = "my-salt-text";


    @Autowired
    public CredentialController(CredentialRepo credentialRepo) {
        this.credentialRepo = credentialRepo;
    }

    @GetMapping("/credential")
    public List<Credential> retrieveAllCredentials() {
        Iterable<Credential> result = credentialRepo.findAll();
        List<Credential> migrationList = new ArrayList<>();
        result.forEach(migrationList::add);
        return migrationList;
    }

    @GetMapping("/credential/{id}")
    public Credential retrieveCredentials(@PathVariable UUID id) {
        Optional<Credential> migration = credentialRepo.findById(id);

        if (!migration.isPresent())
            throw new EntityNotFoundException("id-" + id);

        return migration.get();
    }

    @PostMapping("/credential")
    public ResponseEntity<Object> createCredentials(@RequestBody Credential credential) {
        credential.setId(UUID.randomUUID());
        credential.setPassword(hashPassword(credential.getPassword()));
        Credential savedCredential = credentialRepo.save(credential);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedCredential.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/credential/{id}")
    public ResponseEntity<Object> updateCredentials(@RequestBody Credential credential, @PathVariable UUID id) {
        Optional<Credential> migrationOptional = credentialRepo.findById(id);

        if (!migrationOptional.isPresent())
            return ResponseEntity.notFound().build();

        credential.setId(id);
        credentialRepo.save(credential);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/credential/{id}")
    public ResponseEntity<Object> deleteCredential(@PathVariable UUID id) {
        credentialRepo.deleteById(id);
        return ResponseEntity.ok().build();
    }


    private String hashPassword(String password) {
        String saltedPassword = SALT + password;
        return generateHash(saltedPassword);
    }


    private static String generateHash(String input) {
        StringBuilder hash = new StringBuilder();

        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            byte[] hashedBytes = sha.digest(input.getBytes());
            char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                    'a', 'b', 'c', 'd', 'e', 'f' };
            for (byte b : hashedBytes) {
                hash.append(digits[(b & 0xf0) >> 4]);
                hash.append(digits[b & 0x0f]);
            }
        } catch (NoSuchAlgorithmException e) {
            // handle error here.
        }

        return hash.toString();
    }

}
