package com.task.main.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
    private String roles;

    public List<Long> getRoles() {
        return convertToEntityAttribute(roles);
    }

    public void setRoles(List<Long> roles) {
        this.roles = TaskBuilder.convertToDatabaseColumn(roles);
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public List<Long> convertToEntityAttribute(String data) {
        if (data == null || data.trim().length() == 0) {
            return new ArrayList<>();
        }

        return Arrays.stream(data.split(","))
                .map(Long::valueOf)
                .collect(Collectors.toList());
    }

    public static class TaskBuilder<T> {
        private String roles;

        public TaskBuilder roles(List<Long> roles) {
            this.roles = convertToDatabaseColumn(roles);
            return this;
        }

        public static String convertToDatabaseColumn(List<Long> attribute) {
            if (attribute == null || attribute.isEmpty()) {
                return "";
            }
            return StringUtils.join(attribute, ",");
        }
    }
}