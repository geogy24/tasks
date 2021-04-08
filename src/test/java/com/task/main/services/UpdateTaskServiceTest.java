package com.task.main.services;

import com.github.javafaker.Faker;
import com.task.main.exceptions.*;
import com.task.main.factories.RoleFactory;
import com.task.main.factories.StackFactory;
import com.task.main.factories.TaskFactory;
import com.task.main.models.Task;
import com.task.main.repositories.RoleRepository;
import com.task.main.repositories.StackRepository;
import com.task.main.repositories.TaskRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {UpdateTaskService.class})
public class UpdateTaskServiceTest {

    @MockBean
    private TaskRepository taskRepository;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private StackRepository stackRepository;

    @Autowired
    private UpdateTaskService updateTaskService;

    private TaskFactory taskFactory;

    private Faker faker;

    @Before
    public void setup() {
        this.faker = new Faker();
        this.taskFactory = new TaskFactory();
    }

    @Test
    public void whenExecuteServiceThenReturnTaskCreated() {
        Long id = Long.parseLong(faker.number().digits(3));
        given(this.roleRepository.findAllById(any())).willReturn(List.of(new RoleFactory().model()));
        given(this.stackRepository.findById(anyLong())).willReturn(Optional.of(new StackFactory().model()));
        given(this.taskRepository.findById(anyLong())).willReturn(Optional.of(this.taskFactory.model()));
        given(this.taskRepository.save(any())).willReturn(this.taskFactory.model());

        assertThat(this.updateTaskService.execute(id, this.taskFactory.updateDto())).isNotNull();
    }

    @Test
    public void whenExecuteServiceButRoleNotFoundThenRaiseRoleNotFoundException() {
        Long id = Long.parseLong(faker.number().digits(3));
        given(this.taskRepository.findById(anyLong())).willReturn(Optional.of(this.taskFactory.model()));
        given(this.roleRepository.findAllById(Collections.singleton(anyLong()))).willReturn(List.of());

        assertThrows(RoleNotFoundException.class, () -> this.updateTaskService.execute(id, this.taskFactory.updateDto()));
    }

    @Test
    public void whenExecuteServiceButStackNotFoundThenRaiseStackNotFoundException() {
        Long id = Long.parseLong(faker.number().digits(3));
        given(this.taskRepository.findById(anyLong())).willReturn(Optional.of(this.taskFactory.model()));
        given(this.roleRepository.findAllById(any())).willReturn(List.of(new RoleFactory().model()));
        given(this.stackRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThrows(StackNotFoundException.class, () -> this.updateTaskService.execute(id, this.taskFactory.updateDto()));
    }

    @Test
    public void whenExecuteServiceButParentTaskNotFoundThenRaiseTaskNotFoundException() {
        Long id = Long.parseLong(faker.number().digits(3));
        given(this.roleRepository.findAllById(any())).willReturn(List.of(new RoleFactory().model()));
        given(this.stackRepository.findById(anyLong())).willReturn(Optional.of(new StackFactory().model()));
        given(this.taskRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> this.updateTaskService.execute(id, this.taskFactory.updateDto()));
    }

    @Test
    public void whenExecuteServiceButParentTaskIsNotAParentTaskThenRaiseChildTaskMustNotBeParentTaskException() {
        Long id = Long.parseLong(faker.number().digits(3));
        Task task = taskFactory.model();
        task.setParentTask(new Task());
        given(this.roleRepository.findAllById(any())).willReturn(List.of(new RoleFactory().model()));
        given(this.stackRepository.findById(anyLong())).willReturn(Optional.of(new StackFactory().model()));
        given(this.taskRepository.findById(anyLong())).willReturn(Optional.of(task));
        given(this.taskRepository.save(any())).willReturn(this.taskFactory.model());

        assertThrows(ChildTaskMustNotBeParentTaskException.class, () -> this.updateTaskService.execute(id, this.taskFactory.updateDto()));
    }

    @Test
    public void whenExecuteServiceButParentTaskCanNotBeAChildTaskThenRaiseParentTaskMustNotBeChildTaskException() {
        Long id = Long.parseLong(faker.number().digits(3));
        Task task = taskFactory.model();
        task.setChildTasks(List.of(new Task()));
        given(this.taskRepository.findById(anyLong())).willReturn(Optional.of(task));
        given(this.taskRepository.save(any())).willReturn(this.taskFactory.model());

        assertThrows(ParentTaskMustNotBeChildTaskException.class, () -> this.updateTaskService.execute(id, this.taskFactory.updateDto()));
    }
}
