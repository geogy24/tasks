package com.task.main.services;

import com.github.javafaker.Faker;
import com.task.main.dtos.TaskDto;
import com.task.main.exceptions.*;
import com.task.main.facades.implementations.JoinerFacade;
import com.task.main.facades.models.Joiner;
import com.task.main.factories.JoinerFactory;
import com.task.main.factories.RoleFactory;
import com.task.main.factories.StackFactory;
import com.task.main.factories.TaskFactory;
import com.task.main.models.Task;
import com.task.main.repositories.RoleRepository;
import com.task.main.repositories.StackRepository;
import com.task.main.repositories.TaskRepository;
import com.task.main.services.implementations.CreateTaskService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {CreateTaskService.class})
public class CreateTaskServiceTest {

    private static final Byte FIRST_ELEMENT = 0;

    @MockBean
    private TaskRepository taskRepository;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private StackRepository stackRepository;

    @Autowired
    private CreateTaskService createTaskService;

    @MockBean
    private JoinerFacade joinerFacade;

    private TaskFactory taskFactory;

    private Faker faker;

    @Before
    public void setup() {
        this.faker = new Faker();
        this.taskFactory = new TaskFactory();
    }

    @Test
    public void whenExecuteServiceThenReturnTaskCreated() {
        TaskDto dto = this.taskFactory.dto();
        given(this.joinerFacade.getJoiner(anyLong())).willReturn(Optional.of(new JoinerFactory().model(dto.getRoleIds()[FIRST_ELEMENT])));
        given(this.roleRepository.findAllById(any())).willReturn(List.of(new RoleFactory().model()));
        given(this.stackRepository.findById(anyLong())).willReturn(Optional.of(new StackFactory().model()));
        given(this.taskRepository.findById(anyLong())).willReturn(Optional.of(this.taskFactory.model()));
        given(this.taskRepository.save(any())).willReturn(this.taskFactory.model());

        assertThat(this.createTaskService.execute(dto)).isNotNull();
    }

    @Test
    public void whenExecuteServiceButJoinerNotFoundThenRaiseJoinerNotFoundException() {
        TaskDto dto = this.taskFactory.dto();
        given(this.joinerFacade.getJoiner(anyLong())).willReturn(Optional.empty());

        assertThrows(JoinerNotFoundException.class, () -> this.createTaskService.execute(dto));
    }

    @Test
    public void whenExecuteServiceButJoinerHasNotRequiredRoleThenRaiseJoinerHasNotValidRoleException() {
        TaskDto dto = this.taskFactory.dto();
        Long roleId = Long.parseLong(faker.number().digits(3));
        given(this.joinerFacade.getJoiner(anyLong())).willReturn(Optional.of(new JoinerFactory().model(roleId)));

        assertThrows(JoinerHasNotValidRoleException.class, () -> this.createTaskService.execute(dto));
    }

    @Test
    public void whenExecuteServiceButRoleNotFoundThenRaiseRoleNotFoundException() {
        TaskDto dto = this.taskFactory.dto();
        given(this.joinerFacade.getJoiner(anyLong())).willReturn(Optional.of(new JoinerFactory().model(dto.getRoleIds()[FIRST_ELEMENT])));
        given(this.roleRepository.findAllById(Collections.singleton(anyLong()))).willReturn(List.of());

        assertThrows(RoleNotFoundException.class, () -> this.createTaskService.execute(dto));
    }

    @Test
    public void whenExecuteServiceButStackNotFoundThenRaiseStackNotFoundException() {
        TaskDto dto = this.taskFactory.dto();
        given(this.joinerFacade.getJoiner(anyLong())).willReturn(Optional.of(new JoinerFactory().model(dto.getRoleIds()[FIRST_ELEMENT])));
        given(this.roleRepository.findAllById(any())).willReturn(List.of(new RoleFactory().model()));
        given(this.stackRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThrows(StackNotFoundException.class, () -> this.createTaskService.execute(dto));
    }

    @Test
    public void whenExecuteServiceButParentTaskNotFoundThenRaiseTaskNotFoundException() {
        TaskDto dto = this.taskFactory.dto();
        given(this.joinerFacade.getJoiner(anyLong())).willReturn(Optional.of(new JoinerFactory().model(dto.getRoleIds()[FIRST_ELEMENT])));
        given(this.roleRepository.findAllById(any())).willReturn(List.of(new RoleFactory().model()));
        given(this.stackRepository.findById(anyLong())).willReturn(Optional.of(new StackFactory().model()));
        given(this.taskRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> this.createTaskService.execute(dto));
    }

    @Test
    public void whenExecuteServiceButParentTaskIsNotAParentTaskThenRaiseChildTaskMustNotBeParentTaskException() {
        TaskDto dto = this.taskFactory.dto();
        given(this.joinerFacade.getJoiner(anyLong())).willReturn(Optional.of(new JoinerFactory().model(dto.getRoleIds()[FIRST_ELEMENT])));
        Task task = taskFactory.model();
        task.setParentTask(new Task());
        given(this.roleRepository.findAllById(any())).willReturn(List.of(new RoleFactory().model()));
        given(this.stackRepository.findById(anyLong())).willReturn(Optional.of(new StackFactory().model()));
        given(this.taskRepository.findById(anyLong())).willReturn(Optional.of(task));
        given(this.taskRepository.save(any())).willReturn(this.taskFactory.model());

        assertThrows(ChildTaskMustNotBeParentTaskException.class, () -> this.createTaskService.execute(dto));
    }
}
