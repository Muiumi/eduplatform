package ru.rtstudy.educplatformsecurity.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import ru.rtstudy.educplatformsecurity.model.constant.Role;

@Builder
@Schema(description = "User DTO Response")
public record UserDtoResponse(@Schema(description = "id пользователя", example = "1")
                              Long id,
                              @Schema(description = "email пользователя", example = "surok@gmail.com")
                              String email,
                              @Schema(description = "Имя пользователя", example = "Иван")
                              String firstName,
                              @Schema(description = "Фамилия пользователя", example = "Иванов")
                              String lastName,
                              @Schema(description = "Роль пользователя", example = "MENTOR")
                              Role role) {
}
