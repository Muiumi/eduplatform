package ru.rtstudy.educplatformsecurity.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Request To Change Answer DTO")
public record ChangeStudentAnswerDto(
        @Schema(description = "Ответ студента на задание", example = "Существует два вида буферного кэша")
        @NotBlank(message = "Answer must be filled")
        String studentAnswer) {
}
