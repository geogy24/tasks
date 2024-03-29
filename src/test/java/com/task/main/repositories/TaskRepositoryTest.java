package com.task.main.repositories;

import com.github.javafaker.Faker;
import com.task.main.factories.TaskFactory;
import com.task.main.models.Task;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
public class TaskRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private TaskRepository taskRepository;

    private Task task;

    private Faker faker = new Faker();

    @Before
    public void setup() {
        Long stackId = Long.parseLong(faker.number().digits(3));
        ArrayList<Long> roleIds = new ArrayList<Long>(List.of(Long.parseLong(faker.number().digits(3))));

        TaskFactory taskFactory = new TaskFactory();
        Task task = taskFactory.model();
        task.setRoles(roleIds);
        task.setStack(stackId);

        this.task = this.testEntityManager.persistFlushFind(task);
    }

    @Test
    public void whenGetAllTaskIds() {
        Optional<Long[]> taskIds = this.taskRepository.listAllTasksIds();

        assertThat(taskIds.get()).contains(this.task.getId());
    }
}
