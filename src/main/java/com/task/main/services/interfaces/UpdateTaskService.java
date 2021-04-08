package com.task.main.services.interfaces;

import com.task.main.dtos.UpdateTaskDto;
import com.task.main.models.Task;

public interface UpdateTaskService {
    Task execute(Long id, UpdateTaskDto updateTaskDto);
}
