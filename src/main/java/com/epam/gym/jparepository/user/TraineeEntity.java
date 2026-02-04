package com.epam.gym.jparepository.user;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "trainee", schema = "gym_schema")
public class TraineeEntity {

    @Id
    private UUID uid;

    @Column(name = "address", nullable = false )
    private String address;

    @Column(name = "birthdate", nullable = false, updatable = false)
    private LocalDate birthdate;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "user_uid", unique = true)
    private UserEntity user;
}
