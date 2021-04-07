package com.task.main.services;

import com.github.javafaker.Faker;
import com.task.main.exceptions.ChildTaskMustNotBeParentTaskException;
import com.task.main.exceptions.TaskNotFoundException;
import com.task.main.factories.TaskFactory;
import com.task.main.models.Task;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {ShowTaskService.class})
public class ShowTaskServiceTest {

    @MockBean
    private TaskRepository taskRepository;

    @Autowired
    private ShowTaskService showTaskService;

    private TaskFactory taskFactory;

    private Faker faker;

    @Before
    public void setup() {
        this.faker = new Faker();
        this.taskFactory = new TaskFactory();
    }

    @Test
    public void whenExecuteServiceThenReturnTask() {
        Long id = Long.valueOf(faker.number().digits(2));
        Task task = this.taskFactory.model();
        given(this.taskRepository.findById(anyLong())).willReturn(Optional.of(task));

        Task returnTask = this.showTaskService.execute(id);

        assertThat(returnTask).isNotNull();
        assertThat(returnTask.getName()).isEqualTo(returnTask.getName());
        assertThat(returnTask.getDescription()).isEqualTo(returnTask.getDescription());
        assertThat(returnTask.getEstimatedRequiredHours()).isEqualTo(returnTask.getEstimatedRequiredHours());
    }

    @Test
    public void whenExecuteServiceButTaskNotFoundThenReturnTaskNotFoundException() {
        Long id = Long.valueOf(faker.number().digits(2));

        assertThrows(TaskNotFoundException.class, () -> this.showTaskService.execute(id));
    }
}
