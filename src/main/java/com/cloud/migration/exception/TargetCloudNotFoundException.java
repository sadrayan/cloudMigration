package com.cloud.migration.exception;

public class TargetCloudNotFoundException extends RuntimeException {

    public TargetCloudNotFoundException(String exception) {
        super(exception);
    }

}
