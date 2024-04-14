package ru.rtstudy.educplatformsecurity.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(description = "Grade with Student Info DTO")
@Builder(toBuilder = true)
public record GradeStudentDtoResponse(
        @Schema(description = "Идентификатор выполненного задания", example = "1")
        Long id,
        @Schema(description = "Имя пользователя, выполнившего задание", example = "Пример")
        String firstName,
        @Schema(description = "Фамилия пользователя, выполнившего задание", example = "Примерочный")
        String lastName,
        @Schema(description = "Название урока, к которому было выполнено задание", example = "Буферный кэш")
        String lessonTitle,
        @Schema(description = "Задание к уроку", example = "Сколько уровней буферного кэша есть в PostgreSQL?")
        String description,
        @Schema(description = "Оценка ментора к заданию пользователя", type = "integer", example = "3")
        Byte grade,
        @Schema(description = "Указатель на необходимость исправления своего ответа: true/false", example = "true")
        Boolean rework,
        @Schema(description = "Ответ студента на задание", example = "Существует два вида буферного кэша")
        String studentAnswer,
        @Schema(description = "Комментарий ментора к ответу студента", example = "Нет, дурачьё, подумай еще...")
        String mentorAnswer
) {
}
