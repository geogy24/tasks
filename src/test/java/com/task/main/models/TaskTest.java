package com.task.main.models;

import com.github.javafaker.Faker;
import com.task.main.factories.TaskFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
public class TaskTest {

    @Autowired
    private TestEntityManager testEntityManager;

    private Faker faker;

    @Before
    public void setUp() {
        this.faker = new Faker();
    }

    @Test
    public void whenSaveTaskThenTaskShouldFound() {
        Long stackId = Long.parseLong(faker.number().digits(3));
        ArrayList<Long> roleIds = new ArrayList<Long>(List.of(Long.parseLong(faker.number().digits(3))));

        TaskFactory taskFactory = new TaskFactory();
        Task task = taskFactory.model();
        task.setRoles(roleIds);
        task.setStack(stackId);

        Task savedTask = this.testEntityManager.persistFlushFind(task);

        assertThat(savedTask.getId()).isNotNull();
        assertThat(savedTask.getName()).isEqualTo(task.getName());
        assertThat(savedTask.getDescription()).isEqualTo(task.getDescription());
        assertThat(savedTask.getEstimatedRequiredHours()).isEqualTo(task.getEstimatedRequiredHours());
        assertThat(savedTask.getRoles().isEmpty()).isFalse();
        assertThat(savedTask.getStack()).isEqualTo(stackId);
    }
}
