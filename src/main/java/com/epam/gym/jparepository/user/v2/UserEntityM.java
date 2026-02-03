package com.epam.gym.jparepository.user.v2;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "user", schema = "gym_crm")
public class UserEntityM {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uid;

    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private boolean isActive;
}
