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
import io.swagger.models.auth.In;
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
@ContextConfiguration(classes = {ShowTaskIdsService.class})
public class ShowTaskServiceTest {

    @MockBean
    private TaskRepository taskRepository;

    @Autowired
    private ShowTaskIdsService showTaskService;

    private TaskFactory taskFactory;

    @Before
    public void setup() {
        this.taskFactory = new TaskFactory();
    }

    @Test
    public void whenExecuteServiceThenReturnTaskIds() {
        Long[] list = new Long[]{1L, 2L};
        given(this.taskRepository.listAllTasksIds()).willReturn(Optional.of(list));

        assertThat(this.showTaskService.execute()).contains(list);
    }
}
