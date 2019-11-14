package com.cloud.migration.exception;

public class WorkloadNotFoundException extends RuntimeException {

    public WorkloadNotFoundException(String exception) {
        super(exception);
    }

}
