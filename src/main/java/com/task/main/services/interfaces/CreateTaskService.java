package com.task.main.services.interfaces;

import com.task.main.dtos.TaskDto;
import com.task.main.models.Task;

public interface CreateTaskService {
    Task execute(TaskDto taskDto);
}
