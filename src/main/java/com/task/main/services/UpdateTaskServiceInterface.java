package com.task.main.services;

import com.task.main.dtos.UpdateTaskDto;
import com.task.main.models.Task;

public interface UpdateTaskServiceInterface {
    Task execute(Long id, UpdateTaskDto updateTaskDto);
}
