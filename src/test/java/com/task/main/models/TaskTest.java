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

import java.util.Arrays;
import java.util.HashSet;

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
        Stack stack = new Stack();
        stack.setName(this.faker.lorem().word());
        Stack savedStack = this.testEntityManager.persistAndFlush(stack);

        Role role = new Role();
        role.setName(this.faker.lorem().word());
        Role savedRole = this.testEntityManager.persistFlushFind(role);

        TaskFactory taskFactory = new TaskFactory();
        Task task = taskFactory.model();
        task.setRoles(new HashSet<>(Arrays.asList(savedRole)));
        task.setStack(savedStack);

        Task savedTask = this.testEntityManager.persistFlushFind(task);

        assertThat(savedTask.getId()).isNotNull();
        assertThat(savedTask.getName()).isEqualTo(task.getName());
        assertThat(savedTask.getDescription()).isEqualTo(task.getDescription());
        assertThat(savedTask.getEstimatedRequiredHours()).isEqualTo(task.getEstimatedRequiredHours());
        assertThat(savedTask.getRoles().isEmpty()).isFalse();
        assertThat(savedTask.getStack().getName()).isEqualTo(task.getStack().getName());
    }
}
