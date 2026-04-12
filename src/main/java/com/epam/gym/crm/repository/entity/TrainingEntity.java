package com.epam.gym.crm.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "training", schema = "gym_crm_schema")
public class TrainingEntity {

    @Id
    private UUID uid;

    @Column(name = "name", nullable = false, updatable = false)
    private String name;

    @Column(name = "date", nullable = false, updatable = false)
    private LocalDate date;

    @Column(name = "duration", nullable = false, updatable = false)
    private Integer duration;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "training_type_uid", nullable = false)
    private TrainingTypeEntity trainingType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainee_uid", nullable = false, updatable = false)
    private TraineeEntity trainee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_uid", nullable = false, updatable = false)
    private TrainerEntity trainer;
}
