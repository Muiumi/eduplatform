package ru.rtstudy.educplatformsecurity.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "Task DTO")
public record TaskDtoResponse(

        @Schema(description = "Идентификатор задания", example = "1")
        Long taskId,

        @Schema(description = "Задание к уроку", example = "Сколько уровней буферного кэша есть в PostgreSQL?")
        String description
) {
}
