package com.task.main.services;

import com.task.main.repositories.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ShowTaskIdsService implements ShowTaskIdsServiceInterface {
    private final TaskRepository taskRepository;

    public ShowTaskIdsService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Long[] execute() {
        log.info("Show task ids");
        return this.taskRepository.listAllTasksIds()
                .orElse(new Long[]{});
    }
}
