package com.task.main.facades;

import com.github.javafaker.Faker;
import com.task.main.facades.implementations.RoleFacade;
import com.task.main.facades.models.Role;
import com.task.main.factories.RoleFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doThrow;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {RoleFacade.class})
@TestPropertySource(properties = { "url.joiners=http://localhost:8080" })
public class RoleFacadeTest {

    private RoleFactory roleFactory;

    private Faker faker;

    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    private RoleFacade roleFacade;

    @Before
    public void setup() {
        this.faker = new Faker();
        this.roleFactory = new RoleFactory();
    }

    @Test
    public void whenGetRoleFromRoleMicroserviceThenReturnRole() {
        Long roleId = Long.parseLong(faker.number().digits(3));
        Role role = this.roleFactory.model();
        List<Role> roles = new ArrayList<>(List.of(role));
        given(restTemplate.getForEntity(anyString(), any(), anyMap()))
                .willReturn(new ResponseEntity(roles, HttpStatus.OK));

        Optional<List<Role>> roleResponse = roleFacade.getAllById(new ArrayList<>(){{ add(roleId); }});

        assertEquals(roles, roleResponse.get());
    }

    @Test(expected = RestClientException.class)
    public void whenGetRoleFromRoleMicroserviceButRoleNotFoundThenReturnEmpty() {
        Long roleId = Long.parseLong(faker.number().digits(3));
        doThrow(new RestClientException(null))
                .when(restTemplate)
                .getForEntity(anyString(), any(), anyMap());

        Optional<List<Role>> role = roleFacade.getAllById(new ArrayList<>(){{ add(roleId); }});

        assertThat(role).isNotPresent();
    }

}
