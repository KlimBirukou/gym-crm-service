package com.epam.gym.controller.rest.assignment;

import com.epam.gym.controller.rest.assignment.dto.request.AssignRequest;
import com.epam.gym.controller.rest.trainee.dto.response.TrainerProfileResponse;
import com.epam.gym.controller.rest.trainer.dto.response.TraineeProfileResponse;
import com.epam.gym.facade.assignment.IAssignmentFacade;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/assignment")
@RequiredArgsConstructor
public class AssignmentController {

    private final IAssignmentFacade assignmentFacade;

    @GetMapping("/trainer/{username}/trainees")
    public ResponseEntity<@NonNull List<TraineeProfileResponse>> getTrainees(
        @PathVariable String username,
        @RequestParam Boolean assigned,
        @RequestParam Boolean active
    ) {
        var request = assignmentFacade.getTrainees(username, assigned, active);
        return ResponseEntity.ok(request);
    }

    @GetMapping("/trainee/{username}/trainers")
    public ResponseEntity<@NonNull List<TrainerProfileResponse>> getTrainers(
        @PathVariable String username,
        @RequestParam Boolean assigned,
        @RequestParam Boolean active
    ) {
        var request = assignmentFacade.getTrainers(username, assigned, active);
        return ResponseEntity.ok(request);
    }

    @PostMapping("/assign")
    public ResponseEntity<@NonNull Void> assign(
        @Valid @RequestBody AssignRequest request
    ) {
        assignmentFacade.assign(request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
