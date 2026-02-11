package com.epam.gym.repository.jpa.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "trainee", schema = "gym_schema")
public class TraineeEntity {

    @Id
    private UUID uid;

    @Column(name = "address")
    private String address;

    @Column(name = "birthdate")
    private LocalDate birthdate;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, optional = false)
    @JoinColumn(name = "user_uid", unique = true, nullable = false)
    private UserEntity user;

    @OneToMany(mappedBy = "trainee", cascade = CascadeType.REMOVE)
    private List<TrainingEntity> trainings;

    @OneToMany(mappedBy = "trainee", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<TraineeTrainerEntity> trainers;
}
