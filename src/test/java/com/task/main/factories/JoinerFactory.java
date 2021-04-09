package com.task.main.factories;

import com.github.javafaker.Faker;
import com.task.main.facades.models.Joiner;
import com.task.main.facades.models.Role;

public class JoinerFactory {
    private Faker faker;

    public JoinerFactory() {
        this.faker = new Faker();
    }

    public Joiner model(Long roleId) {
        return Joiner.builder()
                .identificationNumber(this.faker.idNumber().valid())
                .name(this.faker.name().firstName())
                .lastName(this.faker.name().lastName())
                .domainExperience(this.faker.lorem().sentence())
                .role(Role.builder().id(roleId).build())
                .build();
    }

    public Joiner model() {
        return Joiner.builder()
                .identificationNumber(this.faker.idNumber().valid())
                .name(this.faker.name().firstName())
                .lastName(this.faker.name().lastName())
                .domainExperience(this.faker.lorem().sentence())
                .role(Role.builder().id(Long.parseLong(faker.number().digits(3))).build())
                .build();
    }
}
