package ru.rtstudy.educplatformsecurity.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Request To Create/Update Lesson DTO")
public record LessonDtoRequest(

        @Schema(description = "Название урока", example = "Буферный кэш")
        @NotBlank(message = "Title must be filled")
        @Size(max = 255, message = "Title must be equal or under {max} symbols")
        String title,

        @Schema(description = "Название файла с материалам к уроку", example = "lesson1.mp4")
        @NotBlank(message = "File name must be filled")
        @Size(max = 255, message = "File Name must be equal or under {max} symbols")
        String fileName,

        @Schema(description = "Описание урока", example = "На этом уроке вы узнаете что такое буферный кэш, его особенности и как его можно настраивать.")
        @NotBlank(message = "Description must be filled")
        String description,

        @Schema(description = "Id курса, к которому относится данный урок", example = "1")
        @NotNull(message = "Course id must be filled")
        Long courseId
) {
}
