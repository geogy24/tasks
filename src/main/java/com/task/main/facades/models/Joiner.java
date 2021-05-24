package com.task.main.facades.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Joiner {
    Long id;

    String identificationNumber;

    String name;

    String lastName;

    String domainExperience;

    Role role;
}
