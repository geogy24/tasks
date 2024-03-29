package com.task.main.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.task.main.models.Task;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Task dto is using to check task's data
 *
 * @author Jorge Díaz
 * @version 1.0.0
 */
@Data
@Builder
public class TaskDto {
    @NotBlank
    @Size(min = 2, max = 80)
    private String name;

    private String description;

    @JsonProperty(value= "estimated_required_hours")
    @NotNull
    @Min(1)
    private Integer estimatedRequiredHours;

    @JsonProperty(value= "worked_hours")
    @Min(1)
    private Integer workedHours;

    @JsonProperty(value= "joiner_id")
    @NotNull
    @Min(1)
    private Long joinerId;

    @JsonProperty(value= "parent_task_id")
    private Long parentTaskId;

    @JsonProperty(value= "stack_id")
    @NotNull
    @Min(1)
    private Long stackId;

    @JsonProperty(value= "role_ids")
    @NotNull
    private ArrayList<Long> roleIds;

    public Task toTask() {
        return Task.builder()
                .name(this.name)
                .description(Objects.nonNull(this.description) ? this.description : "")
                .estimatedRequiredHours(this.estimatedRequiredHours)
                .workedHours(this.workedHours)
                .stack(this.stackId)
                .roles(this.roleIds)
                .build();
    }
}