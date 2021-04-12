package com.task.main.services;

import com.task.main.exceptions.ChildTaskMustNotBeParentTaskException;
import com.task.main.exceptions.RoleNotFoundException;
import com.task.main.exceptions.StackNotFoundException;
import com.task.main.exceptions.TaskNotFoundException;
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

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {CreateTaskService.class})
public class CreateTaskServiceTest {

    @MockBean
    private TaskRepository taskRepository;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private StackRepository stackRepository;

    @Autowired
    private CreateTaskService createTaskService;

    private TaskFactory taskFactory;

    @Before
    public void setup() {
        this.taskFactory = new TaskFactory();
    }

    @Test
    public void whenExecuteServiceThenReturnTaskCreated() {
        given(this.roleRepository.findById(anyLong())).willReturn(Optional.of(new RoleFactory().model()));
        given(this.stackRepository.findById(anyLong())).willReturn(Optional.of(new StackFactory().model()));
        given(this.taskRepository.findById(anyLong())).willReturn(Optional.of(taskFactory.model()));
        given(this.taskRepository.save(any())).willReturn(this.taskFactory.model());

        assertThat(this.createTaskService.execute(this.taskFactory.dto())).isNotNull();
    }

    @Test
    public void whenExecuteServiceButRoleNotFoundThenRaiseRoleNotFoundException() {
        given(this.roleRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThrows(RoleNotFoundException.class, () -> this.createTaskService.execute(this.taskFactory.dto()));
    }

    @Test
    public void whenExecuteServiceButStackNotFoundThenRaiseStackNotFoundException() {
        given(this.roleRepository.findById(anyLong())).willReturn(Optional.of(new RoleFactory().model()));
        given(this.stackRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThrows(StackNotFoundException.class, () -> this.createTaskService.execute(this.taskFactory.dto()));
    }

    @Test
    public void whenExecuteServiceButParentTaskNotFoundThenRaiseTaskNotFoundException() {
        given(this.roleRepository.findById(anyLong())).willReturn(Optional.of(new RoleFactory().model()));
        given(this.stackRepository.findById(anyLong())).willReturn(Optional.of(new StackFactory().model()));
        given(this.taskRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> this.createTaskService.execute(this.taskFactory.dto()));
    }

    @Test
    public void whenExecuteServiceButParentTaskIsNotAParentTaskThenRaiseChildTaskMustNotBeParentTaskException() {
        Task task = taskFactory.model();
        task.setParentTask(new Task());
        given(this.roleRepository.findById(anyLong())).willReturn(Optional.of(new RoleFactory().model()));
        given(this.stackRepository.findById(anyLong())).willReturn(Optional.of(new StackFactory().model()));
        given(this.taskRepository.findById(anyLong())).willReturn(Optional.of(task));
        given(this.taskRepository.save(any())).willReturn(this.taskFactory.model());

        assertThrows(ChildTaskMustNotBeParentTaskException.class, () -> this.createTaskService.execute(this.taskFactory.dto()));
    }
}
