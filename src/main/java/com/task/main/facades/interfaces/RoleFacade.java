package com.task.main.facades.interfaces;

import com.task.main.facades.models.Role;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface RoleFacade {
    Optional<List<Role>> getAllById(ArrayList<Long> ids);
}
