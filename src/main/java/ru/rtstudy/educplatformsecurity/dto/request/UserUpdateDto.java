package ru.rtstudy.educplatformsecurity.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
@Schema(description = "User update DTO")
public record UserUpdateDto(
        @Schema(description = "Имя пользователя", example = "Иван")
        @NotBlank(message = "Name must be filled")
        @Size(min = 2, max = 255, message = "Name must be between {min} and {max} symbols")
        @Pattern(regexp = "^[а-яА-ЯёЁa-zA-Z]+$", message = "Name can only contain latin or cyrillic symbols")
        String firstName,

        @NotBlank(message = "Surname must be filled")
        @Size(min = 2, max = 255, message = "Surname must be between {min} and {max} symbols")
        @Pattern(regexp = "^[а-яА-ЯёЁa-zA-Z]+$", message = "Surname can only contain latin or cyrillic symbols")
        @Schema(description = "Фамилия пользователя", example = "Иванов")
        String lastName) {
}
