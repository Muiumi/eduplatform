package ru.rtstudy.educplatformsecurity.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Schema(description = "Mentor Review DTO")
@Builder(toBuilder = true)
public record MentorAnswerDtoRequest(
        @Schema(description = "Оценка ментора к заданию пользователя", type = "integer", example = "3")
        @NotNull(message = "Grade must be filled")
        @Min(value = 1, message = "Grade must be at least {value}")
        @Max(value = 10, message = "Grade must be equal or under {value}")
        Byte grade,

        @Schema(description = "Указатель на необходимость исправления своего ответа: true/false", example = "true")
        @NotNull(message = "Rework must be filled")
        Boolean rework,

        @Schema(description = "Комментарий ментора к ответу студента", example = "Нет, дурачьё, подумай еще...")
        @NotBlank(message = "Answer must be filled")
        String mentorAnswer
) {
}
