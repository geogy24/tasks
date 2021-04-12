package com.task.main.services;

import com.task.main.exceptions.TaskNotFoundException;
import com.task.main.models.Task;
import com.task.main.repositories.TaskRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DeleteTaskService implements DeleteTaskServiceInterface {
    private final TaskRepository taskRepository;

    @Autowired
    public DeleteTaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @SneakyThrows
    @Override
    public void execute(Long id) {
        log.info("Delete task by id {}", id);
        Task task = this.taskRepository.findById(id)
                .orElseThrow(TaskNotFoundException::new);
        this.taskRepository.delete(task);
    }
}
