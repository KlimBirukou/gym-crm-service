package com.epam.gym.controller.rest.assignment;

import com.epam.gym.controller.rest.assignment.dto.request.AssignRequest;
import com.epam.gym.controller.rest.trainee.dto.response.TrainerProfileResponse;
import com.epam.gym.controller.rest.trainer.dto.response.TraineeProfileResponse;
import com.epam.gym.facade.assignment.IAssignmentFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/assignment")
@RequiredArgsConstructor
public class AssignmentController {

    private final IAssignmentFacade assignmentFacade;

    @GetMapping("/trainer/{username}/trainees")
    @ResponseStatus(HttpStatus.OK)
    public List<TraineeProfileResponse> getTrainees(
        @PathVariable String username,
        @RequestParam Boolean assigned,
        @RequestParam Boolean active
    ) {
        return assignmentFacade.getTrainees(username, assigned, active);
    }

    @GetMapping("/trainee/{username}/trainers")
    @ResponseStatus(HttpStatus.OK)
    public List<TrainerProfileResponse> getTrainers(
        @PathVariable String username,
        @RequestParam Boolean assigned,
        @RequestParam Boolean active
    ) {
        return assignmentFacade.getTrainers(username, assigned, active);
    }

    @PostMapping("/assign")
    @ResponseStatus(HttpStatus.OK)
    public void assign(
        @Valid @RequestBody AssignRequest request
    ) {
        assignmentFacade.assign(request);
    }
}
