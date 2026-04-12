package com.epam.gym.crm.client.workload;

import com.epam.gym.crm.configuration.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
    name = "gym-workload-server",
    path="/internal/v1/workload",
    configuration = FeignConfiguration.class
)
public interface IWorkloadClient {

    @PostMapping()
    void updateWorkload(@RequestBody TrainerWorkloadRequest request);
}
