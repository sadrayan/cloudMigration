package com.cloud.migration.exception;

public class MigrationNotFoundException extends RuntimeException {

    public MigrationNotFoundException(String exception) {
        super(exception);
    }

}
