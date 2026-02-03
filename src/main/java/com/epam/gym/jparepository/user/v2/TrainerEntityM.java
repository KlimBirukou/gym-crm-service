package com.epam.gym.jparepository.user.v2;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "trainer", schema = "gym_crm")
public class TrainerEntityM {

    @Id
    private UUID uid;

    private String specialization;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_uid", unique = true)
    private UserEntityM user;
}