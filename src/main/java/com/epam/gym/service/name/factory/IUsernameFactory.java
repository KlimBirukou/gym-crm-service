package com.epam.gym.service.name.factory;

public interface IUsernameFactory {

    String create(String firstName, String lastName);

    String create(String firstName, String lastName, int suffix);
}
