package ru.rtstudy.educplatformsecurity.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request to Sign Up DTO")
public class SignUpRequest {

    @Schema(description = "Полное имя нового пользователя", example = "Иван")
    @NotBlank(message = "Name must be filled")
    @Size(min = 2, max = 255, message = "Name must be between {min} and {max} symbols")
    @Pattern(regexp = "^[а-яА-ЯёЁa-zA-Z]+$", message = "Name can only contain latin or cyrillic symbols")
    private String firstName;

    @Schema(description = "Полная фамилия нового пользователя", example = "Иванов")
    @NotBlank(message = "Surname must be filled")
    @Size(min = 2, max = 255, message = "Surname must be between {min} and {max} symbols")
    @Pattern(regexp = "^[а-яА-ЯёЁa-zA-Z]+$", message = "Surname can only contain latin or cyrillic symbols")
    private String lastName;

    @Schema(description = "Электронная почта нового пользователя", example = "surok@gmail.com")
    @NotBlank(message = "Email must be filled")
    @Email(message = "Email address has invalid format: ${validatedValue}",
            regexp = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")
    private String email;

    @Schema(description = "Пароль нового пользователя", example = "secretpassword")
    @NotBlank(message = "Password must be filled")
    @Size(min = 8, max = 255, message = "Password must be between {min} and {max} symbols")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Password can only contain latin symbols or numbers")
    private String password;
}
