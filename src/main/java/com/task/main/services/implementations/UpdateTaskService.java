package com.task.main.services.implementations;

import com.task.main.dtos.UpdateTaskDto;
import com.task.main.exceptions.*;
import com.task.main.facades.interfaces.JoinerFacade;
import com.task.main.facades.interfaces.RoleFacade;
import com.task.main.facades.interfaces.StackFacade;
import com.task.main.facades.models.Joiner;
import com.task.main.facades.models.Role;
import com.task.main.models.Task;
import com.task.main.repositories.TaskRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class UpdateTaskService implements com.task.main.services.interfaces.UpdateTaskService {
    private final TaskRepository taskRepository;
    private final RoleFacade roleFacade;
    private final StackFacade stackFacade;
    private final JoinerFacade joinerFacade;

    @Autowired
    public UpdateTaskService(
            TaskRepository taskRepository,
            RoleFacade roleFacade,
            StackFacade stackFacade,
            JoinerFacade joinerFacade) {
        this.taskRepository = taskRepository;
        this.roleFacade = roleFacade;
        this.stackFacade = stackFacade;
        this.joinerFacade = joinerFacade;
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

        if(Objects.nonNull(updateTaskDto.getJoinerId())) {
            existsJoinerWithRole(task, updateTaskDto);
        }

        Task updateTask = updateTaskDto.toTask(task);
        log.info("Task model built");

        return updateTask;
    }

    @SneakyThrows
    private void checkIfTaskCanBeChild(Task task, UpdateTaskDto updateTaskDto) {
        log.info("Check if task can be child");
        if (Objects.nonNull(task.getChildTasks()) && !task.getChildTasks().isEmpty() &&
                Objects.nonNull(updateTaskDto.getParentTaskId())) {
            throw new ParentTaskMustNotBeChildTaskException();
        }
    }

    @SneakyThrows
    private void checkParent(Task task) {
        log.info("Check if task is parent");
        if (Objects.nonNull(task.getParentTask())) {
            throw new ChildTaskMustNotBeParentTaskException();
        }
    }

    @SneakyThrows
    private void existsRole(ArrayList<Long> roleIds) {
        log.info("Exists roles");
        Optional<List<Role>> roles = this.roleFacade.getAllById(roleIds);

        if (!roles.isPresent() || roles.get().size() != roleIds.size()) {
            throw new RoleNotFoundException();
        }
    }

    @SneakyThrows
    private void existsStack(Long stackId) {
        log.info("Exists stack with Id {}", stackId);
        this.stackFacade.getStack(stackId)
                .orElseThrow(StackNotFoundException::new);
    }

    @SneakyThrows
    private void existsJoinerWithRole(Task task, UpdateTaskDto updateTaskDto) {
        log.info("Check if joiner exists with role");
        Joiner joiner = this.findJoiner(updateTaskDto);
        this.joinerHasValidRole(task, joiner, updateTaskDto);
    }

    @SneakyThrows
    private Joiner findJoiner(UpdateTaskDto updateTaskDto) {
        log.info("Find joiner");
        return this.joinerFacade.getJoiner(updateTaskDto.getJoinerId())
                .orElseThrow(JoinerNotFoundException::new);
    }

    @SneakyThrows
    private void joinerHasValidRole(Task task, Joiner joiner, UpdateTaskDto updateTaskDto) {
        log.info("Check if joiner has a valid role");
        List<Long> roles = this.getRoles(task, updateTaskDto);

        if (!roles.contains(joiner.getRole().getId())) {
            throw new JoinerHasNotValidRoleException();
        }
    }

    private List<Long> getRoles(Task task, UpdateTaskDto updateTaskDto) {
        log.info("Get roles from request and task");
        List<Long> roles = new ArrayList<>();
        if (Objects.nonNull(task.getRoles())) task.getRoles().forEach(id -> roles.add(id));

        if(Objects.nonNull(updateTaskDto.getRoleIds())) {
            updateTaskDto.getRoleIds().forEach(id -> roles.add(id));
        }

        return roles;
    }
}
