package com.task.main.facades.interfaces;

import com.task.main.facades.models.Joiner;

import java.util.Optional;

public interface JoinerFacade {
    Optional<Joiner> getJoiner(Long id);
}
