package ru.rtstudy.educplatformsecurity.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Task DTO")
public record TaskDto(

        @Schema(description = "Идентификатор урока, к которому относится задание", example = "1")
        @NotNull(message = "Lesson id must be filled")
        Long lessonId,

        @Schema(description = "Задание к уроку", example = "Сколько уровней буферного кэша есть в PostgreSQL?")
        @NotBlank(message = "Description must be filled")
        String description
) {
}
