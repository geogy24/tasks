package com.task.main.facades.interfaces;

import com.task.main.facades.models.Stack;

import java.util.Optional;

public interface StackFacade {
    Optional<Stack> getStack(Long id);
}
