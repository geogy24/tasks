package com.task.main.factories;

import com.github.javafaker.Faker;
import com.task.main.facades.models.Role;

public class RoleFactory {
    private Faker faker;

    public RoleFactory() {
        this.faker = new Faker();
    }

    public Role model() {
        return Role.builder()
                .id(Long.parseLong(this.faker.number().digits(3)))
                .name(this.faker.lorem().word())
                .build();
    }

    public Role model(Long roleId) {
        return Role.builder()
                .id(roleId)
                .name(this.faker.lorem().word())
                .build();
    }
}
