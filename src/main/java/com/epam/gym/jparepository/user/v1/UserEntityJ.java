package com.epam.gym.jparepository.user.v1;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "user", schema = "gym_crm")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class UserEntityJ {

    @Id
    @Column(name = "uid", nullable = false, updatable = false)
    private UUID uid;

    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private boolean isActive;
}
