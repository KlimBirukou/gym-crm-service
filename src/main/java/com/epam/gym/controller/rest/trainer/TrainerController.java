package com.epam.gym.controller.rest.trainer;

import com.epam.gym.controller.rest.trainer.dto.request.UpdateTrainerRequest;
import com.epam.gym.controller.rest.trainer.dto.response.TrainerResponse;
import com.epam.gym.facade.trainer.ITrainerFacade;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/trainer")
@RequiredArgsConstructor
public class TrainerController {

    private final ITrainerFacade trainerFacade;

    @GetMapping("/{username}")
    public ResponseEntity<@NonNull TrainerResponse> getTrainer(
        @PathVariable String username
    ) {
        var trainer = trainerFacade.getProfile(username);
        return ResponseEntity.ok(trainer);
    }

    @PutMapping("/{username}")
    public ResponseEntity<@NonNull TrainerResponse> updateTrainer(
        @PathVariable String username,
        @Valid @RequestBody UpdateTrainerRequest request
    ) {
        var trainer = trainerFacade.updateTrainer(username, request);
        return ResponseEntity.ok(trainer);
    }

    @PatchMapping("/status/{username}")
    public ResponseEntity<@NonNull Void> toggleStatus(
        @PathVariable String username,
        @RequestParam Boolean active
    ) {
        trainerFacade.changeStatus(username, active);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
