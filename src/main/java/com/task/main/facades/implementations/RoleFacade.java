package com.task.main.facades.implementations;

import com.task.main.facades.models.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class RoleFacade implements com.task.main.facades.interfaces.RoleFacade {
    private static final String GET_ROLE_URL = "/api/roles";
    private static final String ID_KEY = "id";

    @Value("${url.joiners}")
    private String joinerUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Optional<List<Role>> getAllById(ArrayList<Long> ids) {
        try {
            log.info("Get role from joiners microservice");
            ResponseEntity<List> response = this.restTemplate.getForEntity(this.getRoleUrl(ids), List.class, Map.of());
            log.info("Role {}", response.getBody());
            return Optional.ofNullable(response.getBody());
        } catch (RestClientResponseException restClientResponseException) {
            return Optional.empty();
        }
    }

    private String getRoleUrl(ArrayList<Long> ids) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(joinerUrl + GET_ROLE_URL);
        ids.forEach(id -> builder.queryParam(ID_KEY, id));
        return builder.build().toString();
    }
}
