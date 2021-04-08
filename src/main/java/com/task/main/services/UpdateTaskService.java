package com.task.main.services;

import com.task.main.dtos.UpdateTaskDto;
import com.task.main.exceptions.*;
import com.task.main.models.Role;
import com.task.main.models.Task;
import com.task.main.repositories.RoleRepository;
import com.task.main.repositories.StackRepository;
import com.task.main.repositories.TaskRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class UpdateTaskService implements UpdateTaskServiceInterface {
    private final TaskRepository taskRepository;
    private final RoleRepository roleRepository;
    private final StackRepository stackRepository;

    @Autowired
    public UpdateTaskService(
            TaskRepository taskRepository,
            RoleRepository roleRepository,
            StackRepository stackRepository
    ) {
        this.taskRepository = taskRepository;
        this.roleRepository = roleRepository;
        this.stackRepository = stackRepository;
    }

    @Override
    public Task execute(Long id, UpdateTaskDto updateTaskDto) {
        log.info("Update a task with ID {} data {}", id, updateTaskDto);
        Task task = this.findTask(id);
        Task updateTask = this.makeTaskChanges(task, updateTaskDto);
        log.info("Update task model with data");
        return this.taskRepository.save(updateTask);
    }

    @SneakyThrows
    private Task findTask(Long id) {
        return this.taskRepository.findById(id)
                .orElseThrow(TaskNotFoundException::new);
    }

    @SneakyThrows
    private Task makeTaskChanges(Task task, UpdateTaskDto updateTaskDto) {
        log.info("Build task model with data {}", updateTaskDto);

        this.checkIfTaskCanBeChild(task, updateTaskDto);

        if (Objects.nonNull(updateTaskDto.getParentTaskId())) {
            Task parentSearch = this.findTask(updateTaskDto.getParentTaskId());
            checkParent(parentSearch);
        }

        if(Objects.nonNull(updateTaskDto.getRoleIds())){
            this.existsRole(updateTaskDto.getRoleIds());
        }

        if(Objects.nonNull(updateTaskDto.getStackId())){
            this.existsStack(updateTaskDto.getStackId());
        }

        Task updateTask = updateTaskDto.toTask(task);
        log.info("Task model built");

        return updateTask;
    }

    @SneakyThrows
    private void existsRole(Long[] roleIds) {
        log.info("Exists roles");
        List<Role> roles = (List<Role>) this.roleRepository.findAllById(Arrays.asList(roleIds));

        if (roles.size() != roleIds.length) {
            throw new RoleNotFoundException();
        }
    }

    @SneakyThrows
    private void existsStack(Long stackId) {
        log.info("Exists stack with Id {}", stackId);
        this.stackRepository.findById(stackId)
                .orElseThrow(StackNotFoundException::new);
    }

    @SneakyThrows
    private void checkParent(Task task) {
        log.info("Check if task is parent");
        if (Objects.nonNull(task.getParentTask())) {
            throw new ChildTaskMustNotBeParentTaskException();
        }
    }

    @SneakyThrows
    private void checkIfTaskCanBeChild(Task task, UpdateTaskDto updateTaskDto) {
        if (Objects.nonNull(task.getChildTasks()) && !task.getChildTasks().isEmpty() &&
                Objects.nonNull(updateTaskDto.getParentTaskId())) {
            throw new ParentTaskMustNotBeChildTaskException();
        }
    }
}
