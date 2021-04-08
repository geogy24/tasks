package com.task.main.services.implementations;

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

import java.util.*;

@Service
@Slf4j
public class CreateTaskService implements com.task.main.services.interfaces.CreateTaskService {
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
        log.info("Save task model");
        return this.taskRepository.save(task);
    }

    private Task buildTask(TaskDto taskDto) {
        log.info("Build task model with data {}", taskDto);
        Task task = taskDto.toTask();
        task.setRoles(new HashSet<>(this.findRole(taskDto.getRoleIds())));
        task.setStack(this.findStack(taskDto.getStackId()));

        if (Objects.nonNull(taskDto.getParentTaskId())) {
            Task parentSearch = this.findTask(taskDto.getParentTaskId());
            checkParent(parentSearch);
            task.setParentTask(parentSearch);
        }

        log.info("Task model built");
        return task;
    }

    @SneakyThrows
    private List<Role> findRole(Long[] roleIds) {
        log.info("Find roles");
        List<Role> roles = (List<Role>) this.roleRepository.findAllById(Arrays.asList(roleIds));

        if (roles.size() != roleIds.length) {
            throw new RoleNotFoundException();
        }

        return roles;
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
        log.info("Check if task is parent");
        if (Objects.nonNull(task.getParentTask())) {
            throw new ChildTaskMustNotBeParentTaskException();
        }
    }
}
