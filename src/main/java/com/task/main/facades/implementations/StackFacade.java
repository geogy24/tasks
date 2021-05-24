package com.task.main.facades.implementations;

import com.task.main.facades.models.Stack;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@Slf4j
public class StackFacade implements com.task.main.facades.interfaces.StackFacade {
    private static final String GET_STACK_URL = "/api/stacks/{id}";

    @Value("${url.joiners}")
    private String joinerUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Optional<Stack> getStack(Long id) {
        try {
            log.info("Get stack from joiners microservice");
            ResponseEntity<Stack> response = this.restTemplate.getForEntity(this.getStackUrl(), Stack.class, id);
            log.info("Stack {}", response.getBody());
            return Optional.ofNullable(response.getBody());
        } catch (RestClientResponseException restClientResponseException){
            return Optional.empty();
        }
    }

    private String getStackUrl() {
        return joinerUrl + GET_STACK_URL;
    }
}
