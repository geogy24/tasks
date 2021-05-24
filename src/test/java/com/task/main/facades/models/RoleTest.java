package com.task.main.facades.models;

import com.github.javafaker.Faker;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
public class RoleTest {

    @Autowired
    private TestEntityManager testEntityManager;

    private Faker faker;

    @Before
    public void setUp() {
        this.faker = new Faker();
    }

    @Test
    public void whenCreatesARoleObjectThenGetSameData() {
        Long roleId = Long.parseLong(faker.number().digits(3));
        String name = faker.lorem().word();

        Role role = new Role();
        role.setId(roleId);
        role.setName(name);

        assertThat(role.getId()).isNotNull();
        assertThat(role.getName()).isEqualTo(role.getName());
    }
}
