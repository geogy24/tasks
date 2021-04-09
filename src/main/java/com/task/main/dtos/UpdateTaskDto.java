package com.task.main.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.task.main.models.Role;
import com.task.main.models.Stack;
import com.task.main.models.Task;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Task dto is using to check task's data
 *
 * @author Jorge Díaz
 * @version 1.0.0
 */
@Data
@Builder
public class UpdateTaskDto {
    private static final Byte FIRST_ITEM = 0;
    private static final Byte ITEMS_TO_CONSIDERED = 1;

    @Size(min = 2, max = 80)
    private String name;

    private String description;

    @JsonProperty(value= "estimated_required_hours")
    @Min(1)
    private Integer estimatedRequiredHours;

    @JsonProperty(value= "worked_hours")
    @Min(1)
    private Integer workedHours;

    @JsonProperty(value= "joiner_id")
    @Min(1)
    private Long joinerId;

    @JsonProperty(value= "parent_task_id")
    private Long parentTaskId;

    @JsonProperty(value= "stack_id")
    @Min(1)
    private Long stackId;

    @JsonProperty(value= "role_ids")
    @Min(1)
    private Long[] roleIds;

    public Task toTask(Task task) {
        return Task.builder()
                .id(task.getId())
                .name(Objects.nonNull(this.name) ? this.name : task.getName())
                .description(Objects.nonNull(this.description) ? this.description : task.getDescription())
                .estimatedRequiredHours(Objects.nonNull(this.estimatedRequiredHours) ? this.estimatedRequiredHours : task.getEstimatedRequiredHours())
                .workedHours(Objects.nonNull(this.workedHours) ? this.workedHours : task.getWorkedHours())
                .parentTask(Objects.nonNull(this.parentTaskId) ? Task.builder().id(this.parentTaskId).build() : task.getParentTask())
                .roles(this.getRoles(task))
                .stack(Objects.nonNull(this.stackId) ? Stack.builder().id(this.stackId).build() : task.getStack())
                .joinerId(Objects.nonNull(this.joinerId) ? this.joinerId : task.getJoinerId())
                .build();
    }

    private Set<Role> getRoles(Task task) {
        if (Objects.isNull(this.roleIds)) return task.getRoles();
        Set<Role> roles;

        if (this.roleIds.length > ITEMS_TO_CONSIDERED) {
            roles = new HashSet<>();

            for(Long roleId : this.roleIds) {
                roles.add(Role.builder().id(roleId).build());
            }
        } else {
            if (Objects.isNull(task.getRoles())) task.setRoles(new HashSet<>());
            task.getRoles().add(Role.builder().id(this.roleIds[FIRST_ITEM]).build());
            roles = task.getRoles();
        }

        return roles;
    }
}