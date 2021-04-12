package com.task.main.services;

import com.task.main.dtos.TaskDto;
import com.task.main.models.Task;

public interface CreateTaskServiceInterface {
    Task execute(TaskDto taskDto);
}
