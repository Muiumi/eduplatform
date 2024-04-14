package ru.rtstudy.educplatformsecurity.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "Lesson With Full Description DTO")
public record LessonDtoResponse(

        @Schema(description = "Идентификатор урока", example = "1")
        Long id,
        @Schema(description = "Название урока", example = "Буферный кэш")
        String title,
        @Schema(description = "Название файла с материалам к уроку", example = "21c8bb28f626d28ffcf.mp4")
        String referenceOnFile,
        @Schema(description = "Описание урока", example = "На этом уроке вы узнаете что такое буферный кэш, его особенности и как его можно настраивать.")
        String description,
        @Schema(description = "Id задания, который относится к данному уроку", example = "1")
        Long taskId
) {
}
