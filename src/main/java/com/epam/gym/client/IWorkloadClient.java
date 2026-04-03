package com.epam.gym.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "workload", url = "http://localhost:8081")
public interface IWorkloadClient {

    @PostMapping("/api/v1/workload")
    void updateWorkload(@RequestBody TrainerWorkloadRequest request);
}
