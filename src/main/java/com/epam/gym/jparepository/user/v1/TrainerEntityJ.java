package com.epam.gym.jparepository.user.v1;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "trainer", schema = "gym_crm")
public class TrainerEntityJ extends UserEntityJ {

    private String specialization;
}
