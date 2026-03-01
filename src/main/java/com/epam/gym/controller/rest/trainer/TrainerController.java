package com.epam.gym.controller.rest.trainer;

import com.epam.gym.controller.rest.trainer.dto.request.UpdateTrainerRequest;
import com.epam.gym.controller.rest.trainer.dto.response.TrainerResponse;
import com.epam.gym.facade.trainer.ITrainerFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/trainer")
@RequiredArgsConstructor
public class TrainerController {

    private final ITrainerFacade trainerFacade;

    @GetMapping("/{username}")
    @ResponseStatus(HttpStatus.OK)
    public TrainerResponse getTrainer(
        @PathVariable String username
    ) {
        return trainerFacade.getProfile(username);
    }

    @PutMapping("/{username}")
    @ResponseStatus(HttpStatus.OK)
    public TrainerResponse updateTrainer(
        @PathVariable String username,
        @Valid @RequestBody UpdateTrainerRequest request
    ) {
        return trainerFacade.updateTrainer(username, request);
    }

    @PatchMapping("/status/{username}")
    @ResponseStatus(HttpStatus.OK)
    public void toggleStatus(
        @PathVariable String username,
        @RequestParam Boolean active
    ) {
        trainerFacade.changeStatus(username, active);
    }
}
