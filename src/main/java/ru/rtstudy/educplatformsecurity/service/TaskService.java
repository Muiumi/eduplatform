package ru.rtstudy.educplatformsecurity.service;

import ru.rtstudy.educplatformsecurity.dto.response.TaskDto;
import ru.rtstudy.educplatformsecurity.model.Task;

import java.util.Optional;

public interface TaskService {

    TaskDto getTask(Long id);
}
