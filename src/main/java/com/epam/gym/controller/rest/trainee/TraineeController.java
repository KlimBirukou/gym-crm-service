package com.epam.gym.controller.rest.trainee;

import com.epam.gym.controller.rest.trainee.dto.request.UpdateTraineeRequest;
import com.epam.gym.controller.rest.trainee.dto.response.TraineeResponse;
import com.epam.gym.facade.trainee.ITraineeFacade;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/trainee")
@RequiredArgsConstructor
public class TraineeController {

    private final ITraineeFacade traineeFacade;

    @GetMapping("{username}")
    public ResponseEntity<@NonNull TraineeResponse> getTrainee(
        @PathVariable String username
    ) {
        var trainee = traineeFacade.getProfile(username);
        return ResponseEntity.ok(trainee);
    }

    @PutMapping("/{username}")
    public ResponseEntity<?> updateTrainee(
        @PathVariable String username,
        @Valid @RequestBody UpdateTraineeRequest request
    ) {
        var trainee = traineeFacade.updateTrainee(username, request);
        return ResponseEntity.ok(trainee);
    }

    @PatchMapping("/status/{username}")
    public ResponseEntity<@NonNull Void> toggleStatus(
        @PathVariable String username,
        @RequestParam Boolean active
    ) {
        traineeFacade.changeStatus(username, active);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<@NonNull Void> deleteTrainee(
        @PathVariable String username
    ) {
        traineeFacade.delete(username);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
