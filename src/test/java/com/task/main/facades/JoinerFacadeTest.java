package com.task.main.facades;

import com.github.javafaker.Faker;
import com.task.main.facades.implementations.JoinerFacade;
import com.task.main.facades.models.Joiner;
import com.task.main.factories.JoinerFactory;
import com.task.main.services.implementations.DeleteTaskService;
import javassist.NotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.exceptions.base.MockitoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
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
@ContextConfiguration(classes = {JoinerFacade.class})
public class JoinerFacadeTest {

    private JoinerFactory joinerFactory;

    private Faker faker;

    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    private JoinerFacade joinerFacade;

    @Before
    public void setup() {
        this.faker = new Faker();
        this.joinerFactory = new JoinerFactory();
    }

    @Test
    public void whenGetJoinerFromJoinerMicroserviceThenReturnJoiner() {
        Long joinerId = Long.parseLong(faker.number().digits(3));
        Joiner joiner = this.joinerFactory.model();
        given(restTemplate.getForEntity(anyString(), any(), anyLong()))
                .willReturn(new ResponseEntity(joiner, HttpStatus.OK));

        Optional<Joiner> joinerResponse = joinerFacade.getJoiner(joinerId);

        assertEquals(joiner, joinerResponse.get());
    }

    @Test(expected = RestClientException.class)
    public void whenGetJoinerFromJoinerMicroserviceButJoinerNotFoundThenReturnEmpty() {
        Long joinerId = Long.parseLong(faker.number().digits(3));
        doThrow(new RestClientException(null))
                .when(restTemplate)
                .getForEntity(anyString(), any(), anyLong());

        Optional<Joiner> joiner = joinerFacade.getJoiner(joinerId);

        assertThat(joiner.isPresent()).isFalse();
    }

}
