package com.task.main.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * Task model is using to save task's data
 *
 * @author Jorge Díaz
 * @version 1.0.0
 */
@Entity
@Table(name = "tasks", schema = "tasks")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Task {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private long id;

    @Column(nullable = false)
    @NotBlank
    private String name;

    @Column
    private String description;

    @Column(nullable = false)
    @Min(1)
    @JsonProperty("estimated_required_hours")
    private Integer estimatedRequiredHours;

    @Min(1)
    @JsonProperty("worked_hours")
    private Integer workedHours;

    @Min(1)
    @JsonProperty("joiner_id")
    private Long joinerId;

    @ManyToOne
    @JsonProperty("parent_task")
    private Task parentTask;

    @Column(nullable = false)
    private Long stack;

    @OneToMany(mappedBy = "parentTask")
    @JsonProperty("child_tasks")
    private List<Task> childTasks;

    @Column(nullable = false)
    private ArrayList<Long> roles;
}