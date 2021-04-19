package com.task.main.services;

import com.github.javafaker.Faker;
import com.task.main.dtos.UpdateTaskDto;
import com.task.main.exceptions.*;
import com.task.main.facades.implementations.JoinerFacade;
import com.task.main.facades.implementations.RoleFacade;
import com.task.main.facades.implementations.StackFacade;
import com.task.main.factories.JoinerFactory;
import com.task.main.factories.RoleFactory;
import com.task.main.factories.StackFactory;
import com.task.main.factories.TaskFactory;
import com.task.main.models.Task;
import com.task.main.repositories.TaskRepository;
import com.task.main.services.implementations.UpdateTaskService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
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

    private static final Byte FIRST_ELEMENT = 0;

    @MockBean
    private TaskRepository taskRepository;

    @MockBean
    private RoleFacade roleFacade;

    @MockBean
    private StackFacade stackFacade;

    @MockBean
    private JoinerFacade joinerFacade;

    @Autowired
    private UpdateTaskService updateTaskService;

    private TaskFactory taskFactory;

    private JoinerFactory joinerFactory;

    private Faker faker;

    @Before
    public void setup() {
        this.faker = new Faker();
        this.taskFactory = new TaskFactory();
        this.joinerFactory = new JoinerFactory();
    }

    @Test
    public void whenExecuteServiceThenReturnNotContent() {
        Long id = Long.parseLong(faker.number().digits(3));
        UpdateTaskDto updateDto = this.taskFactory.updateDto();
        given(this.joinerFacade.getJoiner(anyLong())).willReturn(Optional.of(new JoinerFactory().model(updateDto.getRoleIds().get(FIRST_ELEMENT))));
        given(this.roleFacade.getAllById(any())).willReturn(Optional.of(List.of(new RoleFactory().model())));
        given(this.stackFacade.getStack(anyLong())).willReturn(Optional.of(new StackFactory().model()));
        given(this.taskRepository.findById(anyLong())).willReturn(Optional.of(this.taskFactory.model()));
        given(this.taskRepository.save(any())).willReturn(this.taskFactory.model());

        assertThat(this.updateTaskService.execute(id, updateDto)).isNotNull();
    }

    @Test
    public void whenExecuteServiceWithMultipleRolesThenReturnNotContent() {
        Long id = Long.parseLong(faker.number().digits(3));
        UpdateTaskDto updateTaskDto = this.taskFactory.updateDto();
        updateTaskDto.setRoleIds(new ArrayList<>(List.of(Long.parseLong(faker.number().digits(3)), Long.parseLong(faker.number().digits(3)))));
        given(this.joinerFacade.getJoiner(anyLong())).willReturn(Optional.of(new JoinerFactory().model(updateTaskDto.getRoleIds().get(FIRST_ELEMENT))));
        given(this.roleFacade.getAllById(any())).willReturn(Optional.of(List.of(new RoleFactory().model(), new RoleFactory().model())));
        given(this.stackFacade.getStack(anyLong())).willReturn(Optional.of(new StackFactory().model()));
        given(this.taskRepository.findById(anyLong())).willReturn(Optional.of(this.taskFactory.model()));
        given(this.taskRepository.save(any())).willReturn(this.taskFactory.model());

        assertThat(this.updateTaskService.execute(id, updateTaskDto)).isNotNull();
    }

    @Test
    public void whenExecuteServiceButRoleNotFoundThenRaiseRoleNotFoundException() {
        Long id = Long.parseLong(faker.number().digits(3));
        given(this.taskRepository.findById(anyLong())).willReturn(Optional.of(this.taskFactory.model()));
        given(this.roleFacade.getAllById(any())).willReturn(Optional.of(List.of()));

        assertThrows(RoleNotFoundException.class, () -> this.updateTaskService.execute(id, this.taskFactory.updateDto()));
    }

    @Test
    public void whenExecuteServiceButStackNotFoundThenRaiseStackNotFoundException() {
        Long id = Long.parseLong(faker.number().digits(3));
        given(this.taskRepository.findById(anyLong())).willReturn(Optional.of(this.taskFactory.model()));
        given(this.roleFacade.getAllById(any())).willReturn(Optional.of(List.of(new RoleFactory().model())));
        given(this.stackFacade.getStack(anyLong())).willReturn(Optional.empty());

        assertThrows(StackNotFoundException.class, () -> this.updateTaskService.execute(id, this.taskFactory.updateDto()));
    }

    @Test
    public void whenExecuteServiceButParentTaskNotFoundThenRaiseTaskNotFoundException() {
        Long id = Long.parseLong(faker.number().digits(3));
        given(this.roleFacade.getAllById(any())).willReturn(Optional.of(List.of(new RoleFactory().model())));
        given(this.stackFacade.getStack(anyLong())).willReturn(Optional.of(new StackFactory().model()));
        given(this.taskRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> this.updateTaskService.execute(id, this.taskFactory.updateDto()));
    }

    @Test
    public void whenExecuteServiceButParentTaskIsNotAParentTaskThenRaiseChildTaskMustNotBeParentTaskException() {
        Long id = Long.parseLong(faker.number().digits(3));
        Task task = taskFactory.model();
        task.setParentTask(new Task());
        given(this.roleFacade.getAllById(any())).willReturn(Optional.of(List.of(new RoleFactory().model())));
        given(this.stackFacade.getStack(anyLong())).willReturn(Optional.of(new StackFactory().model()));
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

    @Test
    public void whenExecuteServiceButJoinerNotFoundThenRaiseJoinerNotFoundException() {
        Long id = Long.parseLong(faker.number().digits(3));
        UpdateTaskDto updateTaskDto = this.taskFactory.updateDto();
        updateTaskDto.setRoleIds(new ArrayList<>(List.of(Long.parseLong(faker.number().digits(3)), Long.parseLong(faker.number().digits(3)))));
        given(this.joinerFacade.getJoiner(anyLong())).willReturn(Optional.empty());
        given(this.roleFacade.getAllById(any())).willReturn(Optional.of(List.of(new RoleFactory().model(), new RoleFactory().model())));
        given(this.stackFacade.getStack(anyLong())).willReturn(Optional.of(new StackFactory().model()));
        given(this.taskRepository.findById(anyLong())).willReturn(Optional.of(this.taskFactory.model()));

        assertThrows(JoinerNotFoundException.class, () -> this.updateTaskService.execute(id, updateTaskDto));
    }

    @Test
    public void whenExecuteServiceButJoinerHasNotValidRoleThenRaiseJoinerHasNotValidRoleException() {
        Long id = Long.parseLong(faker.number().digits(3));
        Long roleId = Long.parseLong(faker.number().digits(3));
        UpdateTaskDto updateTaskDto = this.taskFactory.updateDto();
        updateTaskDto.setRoleIds(null);
        given(this.joinerFacade.getJoiner(anyLong())).willReturn(Optional.of(this.joinerFactory.model(Long.parseLong(faker.number().digits(3)))));
        given(this.roleFacade.getAllById(any())).willReturn(Optional.of(List.of(new RoleFactory().model(roleId))));
        given(this.stackFacade.getStack(anyLong())).willReturn(Optional.of(new StackFactory().model()));
        given(this.taskRepository.findById(anyLong())).willReturn(Optional.of(this.taskFactory.model(roleId)));

        assertThrows(JoinerHasNotValidRoleException.class, () -> this.updateTaskService.execute(id, updateTaskDto));
    }
}
