package com.task.main.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Set;

/**
 * Task model is using to save task's data
 *
 * @author Jorge Díaz
 * @version 1.0.0
 */
@Entity
@Table(name = "tasks")
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
    private Integer estimatedRequiredHours;

    @Min(1)
    private Integer workedHours;

    @ManyToOne
    private Task parentTask;

    @ManyToOne
    private Stack stack;

    @OneToMany(mappedBy = "parentTask")
    private List<Task> childTasks;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(name = "tasks_by_roles",
            joinColumns = {
                    @JoinColumn(name = "task_id", referencedColumnName = "id",
                            nullable = false, updatable = false)},
            inverseJoinColumns = {
                    @JoinColumn(name = "role_id", referencedColumnName = "id",
                            nullable = false, updatable = false)})
    private Set<Role> roles;
}