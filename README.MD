The core business service of the system. Manages the full lifecycle of trainers and trainees:

- registration and profile management for trainers and trainees;
- assigning one or multiple trainers to a trainee;
- creating, editing, and cancelling training sessions.

Login is performed in cooperation with Auth Service. When a training session is created, its details are forwarded to Workload Service for workload tracking. When a trainee is deleted, all of their upcoming (not yet conducted) training sessions are extracted, transformed into the appropriate format, and sent to Workload Service to be removed from the trainers' statistics.
