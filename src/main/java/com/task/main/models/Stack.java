package com.task.main.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

/**
 * Stacks model is using to save stack's data
 *
 * @author Jorge Díaz
 * @version 1.0.0
 */
@Entity
@Table(name = "stacks")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Stack {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private long id;

    @Column(nullable = false)
    @NotBlank
    private String name;
}