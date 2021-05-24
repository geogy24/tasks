package com.task.main.facades;

import com.github.javafaker.Faker;
import com.task.main.facades.implementations.StackFacade;
import com.task.main.facades.models.Stack;
import com.task.main.factories.StackFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doThrow;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {StackFacade.class})
public class StackFacadeTest {

    private StackFactory stackFactory;

    private Faker faker;

    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    private StackFacade stackFacade;

    @Before
    public void setup() {
        this.faker = new Faker();
        this.stackFactory = new StackFactory();
    }

    @Test
    public void whenGetStackFromStackMicroserviceThenReturnStack() {
        Long joinerId = Long.parseLong(faker.number().digits(3));
        Stack joiner = this.stackFactory.model();
        given(restTemplate.getForEntity(anyString(), any(), anyLong()))
                .willReturn(new ResponseEntity(joiner, HttpStatus.OK));

        Optional<Stack> stackResponse = stackFacade.getStack(joinerId);

        assertEquals(joiner, stackResponse.get());
    }

    @Test(expected = RestClientException.class)
    public void whenGetStackFromStackMicroserviceButStackNotFoundThenReturnEmpty() {
        Long joinerId = Long.parseLong(faker.number().digits(3));
        doThrow(new RestClientException(null))
                .when(restTemplate)
                .getForEntity(anyString(), any(), anyLong());

        Optional<Stack> joiner = stackFacade.getStack(joinerId);

        assertThat(joiner).isNotPresent();
    }

}
