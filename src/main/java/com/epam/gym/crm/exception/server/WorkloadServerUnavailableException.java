package com.epam.gym.crm.exception.server;

public class WorkloadServerUnavailableException extends ServerUnavailableException {

    public WorkloadServerUnavailableException() {
        super("gym workload server");
    }
}
