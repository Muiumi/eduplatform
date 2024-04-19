package ru.rtstudy.educplatformsecurity.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Grade For Student DTO")
public record AllStudentAnswers(
        @Schema(description = "Идентификатор выполненного задания", example = "1")
        Long id,
        @Schema(description = "Задание к уроку", example = "Сколько уровней буферного кэша есть в PostgreSQL?")
        String task,
        @Schema(description = "Оценка ментора к заданию пользователя", type = "integer", example = "3")
        Byte grade,
        @Schema(description = "Указатель на необходимость исправления своего ответа: true/false", example = "true")
        Boolean rework,
        @Schema(description = "Ответ студента на задание", example = "Существует два вида буферного кэша")
        String studentAnswer,
        @Schema(description = "Комментарий ментора к ответу студента", example = "Нет, дурачьё, подумай еще...")
        String mentorAnswer) {
}
