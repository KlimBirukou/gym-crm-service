package com.epam.gym.repository.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "trainer", schema = "gym_schema")
public class TrainerEntity {

    @Id
    private UUID uid;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "training_type_uid", nullable = false)
    private TrainingTypeEntity specialization;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "user_uid", unique = true, nullable = false)
    private UserEntity user;

    @OneToMany(mappedBy = "trainer", cascade = CascadeType.REMOVE)
    private List<TrainingEntity> trainings;

    @OneToMany(mappedBy = "trainer", cascade = CascadeType.REMOVE)
    private List<TraineeTrainerEntity> trainees;
}