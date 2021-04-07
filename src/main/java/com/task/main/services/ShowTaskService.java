package com.task.main.services;

import com.task.main.exceptions.TaskNotFoundException;
import com.task.main.models.Task;
import com.task.main.repositories.TaskRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ShowTaskService implements ShowTaskServiceInterface {
    private final TaskRepository taskRepository;

    public ShowTaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    @SneakyThrows
    public Task execute(Long id) {
        log.info("Show task by id {}", id);
        return this.taskRepository.findByIdAndActive(id, true)
                .orElseThrow(TaskNotFoundException::new);
    }
}
