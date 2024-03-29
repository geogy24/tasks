package com.task.main.facades.implementations;

import com.task.main.facades.models.Joiner;
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
public class JoinerFacade implements com.task.main.facades.interfaces.JoinerFacade {
    private static final String GET_JOINER_URL = "/api/joiners/{id}";

    @Value("${url.joiners}")
    private String joinerUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Optional<Joiner> getJoiner(Long id) {
        try {
            log.info("Get joiner from joiners microservice");
            ResponseEntity<Joiner> response = this.restTemplate.getForEntity(this.getJoinerUrl(), Joiner.class, id);
            log.info("Joiner {}", response.getBody());
            return Optional.ofNullable(response.getBody());
        } catch (RestClientResponseException restClientResponseException){
            return Optional.empty();
        }
    }

    private String getJoinerUrl() {
        return joinerUrl + GET_JOINER_URL;
    }
}
