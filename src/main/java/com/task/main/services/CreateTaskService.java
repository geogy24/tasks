package com.task.main.services;

import com.task.main.dtos.TaskDto;
import com.task.main.exceptions.ChildTaskMustNotBeParentTaskException;
import com.task.main.exceptions.RoleNotFoundException;
import com.task.main.exceptions.StackNotFoundException;
import com.task.main.exceptions.TaskNotFoundException;
import com.task.main.models.Role;
import com.task.main.models.Stack;
import com.task.main.models.Task;
import com.task.main.repositories.RoleRepository;
import com.task.main.repositories.StackRepository;
import com.task.main.repositories.TaskRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;

@Service
@Slf4j
public class CreateTaskService implements CreateTaskServiceInterface {
    private final RoleRepository roleRepository;
    private final StackRepository stackRepository;
    private final TaskRepository taskRepository;

    public CreateTaskService(
            RoleRepository roleRepository,
            StackRepository stackRepository,
            TaskRepository taskRepository) {
        this.roleRepository = roleRepository;
        this.stackRepository = stackRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public Task execute(TaskDto taskDto) {
        log.info("Create a task from service with data {}", taskDto);
        Task task = this.buildTask(taskDto);
        log.info("Save task model with data {}", task);
        return this.taskRepository.save(task);
    }

    private Task buildTask(TaskDto taskDto) {
        log.info("Build task model with data {}", taskDto);
        Task task = taskDto.toTask();
        task.setRoles(new HashSet<>(Collections.singletonList(this.findRole(taskDto.getRoleId()))));
        task.setStack(this.findStack(taskDto.getStackId()));

        if (Objects.nonNull(taskDto.getParentTaskId())) {
            Task parentSearch = this.findTask(taskDto.getParentTaskId());
            checkParent(parentSearch);
            task.setParentTask(parentSearch);
        }

        log.info("Task model built {}", task);
        return task;
    }

    @SneakyThrows
    private Role findRole(Long roleId) {
        log.info("Find role with Id {}", roleId);
        return this.roleRepository.findById(roleId)
                .orElseThrow(RoleNotFoundException::new);
    }

    @SneakyThrows
    private Stack findStack(Long stackId) {
        log.info("Find stack with Id {}", stackId);
        return this.stackRepository.findById(stackId)
                .orElseThrow(StackNotFoundException::new);
    }

    @SneakyThrows
    private Task findTask(Long taskId) {
        log.info("Find task with Id {}", taskId);
        return this.taskRepository.findById(taskId)
                .orElseThrow(TaskNotFoundException::new);
    }

    @SneakyThrows
    private void checkParent(Task task) {
        log.info("Check if task is parent {}", task);
        if (Objects.nonNull(task.getParentTask())) {
            throw new ChildTaskMustNotBeParentTaskException();
        }
    }
}
