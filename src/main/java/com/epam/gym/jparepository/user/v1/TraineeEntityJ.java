package com.epam.gym.jparepository.user.v1;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "trainee", schema = "gym_crm")
public class TraineeEntityJ extends UserEntityJ {

    private String address;
    private LocalDate birthdate;
}
