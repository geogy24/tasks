package com.task.main.services;

import com.task.main.factories.TaskFactory;
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
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {ShowTaskIdsService.class})
public class ShowTaskIdsServiceTest {

    @MockBean
    private TaskRepository taskRepository;

    @Autowired
    private ShowTaskIdsService showTaskIdsService;

    private TaskFactory taskFactory;

    @Before
    public void setup() {
        this.taskFactory = new TaskFactory();
    }

    @Test
    public void whenExecuteServiceThenReturnTaskIds() {
        Long[] list = new Long[]{1L, 2L};
        given(this.taskRepository.listAllTasksIds()).willReturn(Optional.of(list));

        assertThat(this.showTaskIdsService.execute()).contains(list);
    }
}
