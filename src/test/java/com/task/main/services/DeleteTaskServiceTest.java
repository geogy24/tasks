package com.task.main.services;

import com.github.javafaker.Faker;
import com.task.main.exceptions.TaskNotFoundException;
import com.task.main.factories.TaskFactory;
import com.task.main.models.Task;
import com.task.main.repositories.TaskRepository;
import com.task.main.services.implementations.DeleteTaskService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {DeleteTaskService.class})
public class DeleteTaskServiceTest {

    @MockBean
    private TaskRepository taskRepository;

    @Autowired
    private DeleteTaskService deleteTaskService;

    private TaskFactory taskFactory;

    private Faker faker;

    @Before
    public void setup() {
        this.faker = new Faker();
        this.taskFactory = new TaskFactory();
    }

    @Test
    public void whenExecuteServiceThenDeleteTaskIsCall() {
        Long id = Long.valueOf(faker.number().digits(2));
        Task task = this.taskFactory.model();
        given(this.taskRepository.findById(anyLong())).willReturn(Optional.of(task));

        this.deleteTaskService.execute(id);

        verify(this.taskRepository, times(1)).findById(any());
        verify(this.taskRepository, times(1)).delete(any());
    }

    @Test
    public void whenExecuteServiceButTaskNotFoundThenReturnTaskNotFoundException() {
        Long id = Long.valueOf(faker.number().digits(2));

        assertThrows(TaskNotFoundException.class, () -> this.deleteTaskService.execute(id));
        verify(this.taskRepository, times(0)).delete(any());
    }

}
