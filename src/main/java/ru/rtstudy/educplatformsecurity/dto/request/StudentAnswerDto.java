package ru.rtstudy.educplatformsecurity.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Student Answer For Task DTO")
public record StudentAnswerDto(

        @Schema(description = "Идентификатор урока, к которому выполнено задание", example = "1")
        @NotNull(message = "Lesson id must be filled")
        @Max(value = Long.MAX_VALUE, message = "Lesson id must be equal or under {value}")
        Long lessonId,

        @Schema(description = "Ответ студента на задание", example = "Существует два вида буферного кэша")
        @NotBlank(message = "Student answer must be filled")
        String studentAnswer) {
}
