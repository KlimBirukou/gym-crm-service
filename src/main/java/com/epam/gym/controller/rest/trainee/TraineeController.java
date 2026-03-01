package com.epam.gym.controller.rest.trainee;

import com.epam.gym.controller.rest.trainee.dto.request.UpdateTraineeRequest;
import com.epam.gym.controller.rest.trainee.dto.response.TraineeResponse;
import com.epam.gym.facade.trainee.ITraineeFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/trainee")
@RequiredArgsConstructor
public class TraineeController {

    private final ITraineeFacade traineeFacade;

    @GetMapping("{username}")
    @ResponseStatus(HttpStatus.OK)
    public TraineeResponse getTrainee(
        @PathVariable String username
    ) {
        return traineeFacade.getProfile(username);
    }

    @PutMapping("/{username}")
    @ResponseStatus(HttpStatus.OK)
    public TraineeResponse updateTrainee(
        @PathVariable String username,
        @Valid @RequestBody UpdateTraineeRequest request
    ) {
        return traineeFacade.updateTrainee(username, request);
    }

    @PatchMapping("/status/{username}")
    @ResponseStatus(HttpStatus.OK)
    public void toggleStatus(
        @PathVariable String username,
        @RequestParam Boolean active
    ) {
        traineeFacade.changeStatus(username, active);
    }

    @DeleteMapping("/{username}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTrainee(
        @PathVariable String username
    ) {
        traineeFacade.delete(username);
    }
}
