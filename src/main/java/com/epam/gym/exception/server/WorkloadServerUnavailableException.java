package com.epam.gym.exception.server;

public class WorkloadServerUnavailableException extends ServerUnavailableException {

    public WorkloadServerUnavailableException() {
        super("gym workload server");
    }
}
