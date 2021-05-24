package com.task.main.factories;

import com.github.javafaker.Faker;
import com.task.main.facades.models.Stack;

public class StackFactory {
    private Faker faker;

    public StackFactory() {
        this.faker = new Faker();
    }

    public Stack model() {
        return Stack.builder()
                .id(Long.parseLong(this.faker.number().digits(3)))
                .name(this.faker.lorem().word())
                .build();
    }
}
