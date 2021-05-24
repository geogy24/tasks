package com.task.main.repositories.dtos;

public interface TaskDto {
    Long getId();

    String getName();

    String getDescription();

    Integer getEstimated_required_hours();

    Integer getWorked_hours();

    Long getJoiner_id();

    TaskDto getParent_task();

    Long getStack();
    
    String getRoles();
}